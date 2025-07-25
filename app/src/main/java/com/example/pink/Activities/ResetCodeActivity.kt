package com.example.pink.Activities

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.example.pink.Fragment.SmsBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class ResetCodeActivity : AppCompatActivity() {

    private lateinit var verify1: TextInputLayout
    private lateinit var verify2: TextInputLayout
    private lateinit var verify3: TextInputLayout
    private lateinit var verify4: TextInputLayout
    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    private lateinit var userID: String
    private val userRef = FirebaseFirestore.getInstance()

    companion object {
        private const val REQ_USER_CONSENT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_code)

        verify1 = findViewById(R.id.verify_1)
        verify2 = findViewById(R.id.verify_2)
        verify3 = findViewById(R.id.verify_3)
        verify4 = findViewById(R.id.verify_4)

        userID = intent.getStringExtra("userID") ?: ""

        setupOtpFields()
        startSmsUserConsent()
    }

    private fun setupOtpFields() {
        val fields = listOf(verify1, verify2, verify3, verify4)

        fields.forEachIndexed { index, currentField ->
            currentField.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        currentField.error = null
                        if (index < fields.lastIndex) {
                            fields[index + 1].editText?.requestFocus()
                        } else {
                            validateDataOnBtnClick() // Verifica al escribir el último dígito
                        }
                    } else if (s?.isEmpty() == true && index > 0) {
                        fields[index - 1].editText?.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun validateDataOnBtnClick() {
        val otpDigits = listOf(
            verify1.editText?.text?.toString()?.trim(),
            verify2.editText?.text?.toString()?.trim(),
            verify3.editText?.text?.toString()?.trim(),
            verify4.editText?.text?.toString()?.trim()
        )

        if (otpDigits.any { it.isNullOrEmpty() }) {
            listOf(verify1, verify2, verify3, verify4).forEachIndexed { index, field ->
                if (otpDigits[index].isNullOrEmpty()) {
                    field.error = "Required!"
                } else {
                    field.error = null
                }
            }
            return
        }

        val otpMerged = otpDigits.joinToString("")

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                    val storedOtp = documentSnapshot.getString("UserOTP")
                    if (otpMerged == storedOtp) {
                        val intent = Intent(this, ResetPasswordActivity::class.java)
                        intent.putExtra("userID", userID)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect Reset Code!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error verifying code: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getOtpFromMessage(message: String) {
        val pattern = Pattern.compile("\\d{4}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            val otp = matcher.group(0)
            if (!otp.isNullOrEmpty() && otp.length == 4) {
                verify1.editText?.setText("${otp[0]}")
                verify2.editText?.setText("${otp[1]}")
                verify3.editText?.setText("${otp[2]}")
                verify4.editText?.setText("${otp[3]}")
                validateDataOnBtnClick()
            }
        }
    }

    private fun startSmsUserConsent() {
        val client: SmsRetrieverClient = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
            .addOnSuccessListener { /* Consent started */ }
            .addOnFailureListener { /* Handle failure silently */ }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT && resultCode == Activity.RESULT_OK && data != null) {
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let { getOtpFromMessage(it) }
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().apply {
            smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT)
                    }

                    override fun onFailure() { /* Can log error if needed */
                    }
                }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        smsBroadcastReceiver?.let { unregisterReceiver(it) }
    }
}