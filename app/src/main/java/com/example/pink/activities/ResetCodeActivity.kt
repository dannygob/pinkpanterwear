package com.example.pink.activities

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.example.pink.fragment.SmsBroadcastReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class ResetCodeActivity : AppCompatActivity() {

    private lateinit var verifyFields: List<TextInputLayout>
    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private lateinit var userID: String
    private val userRef = FirebaseFirestore.getInstance()

    // ✅ Nuevo launcher para SMS consent
    private val consentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                message?.let { getOtpFromMessage(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_code)

        verifyFields = listOf(
            findViewById(R.id.verify_1),
            findViewById(R.id.verify_2),
            findViewById(R.id.verify_3),
            findViewById(R.id.verify_4)
        )

        userID = intent.getStringExtra("userID") ?: ""

        setupOtpFields()
        startSmsUserConsent()
    }

    private fun setupOtpFields() {
        verifyFields.forEachIndexed { index, field ->
            field.editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    when {
                        s?.length == 1 -> {
                            field.error = null
                            if (index < verifyFields.lastIndex) {
                                verifyFields[index + 1].editText?.requestFocus()
                            } else {
                                validateOtp()
                            }
                        }

                        s.isNullOrEmpty() && index > 0 -> {
                            verifyFields[index - 1].editText?.requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun validateOtp() {
        val otp = verifyFields.map { it.editText?.text?.toString()?.trim() }

        if (otp.any { it.isNullOrEmpty() }) {
            verifyFields.forEachIndexed { i, field ->
                field.error = if (otp[i].isNullOrEmpty()) getString(R.string.required) else null
            }
            return
        }

        val otpMerged = otp.joinToString("")

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { doc ->
                val storedOtp = doc.getString("UserOTP")
                if (otpMerged == storedOtp) {
                    Intent(this, ResetPasswordActivity::class.java).apply {
                        putExtra("userID", userID)
                        startActivity(this)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.incorrect_reset_code),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.error_verifying_code, it.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getOtpFromMessage(message: String) {
        val matcher = Pattern.compile("\\d{4}").matcher(message)
        if (matcher.find()) {
            val otp = matcher.group(0)
            otp?.forEachIndexed { index, digit ->
                verifyFields[index].editText?.setText(digit.toString())
            }
            validateOtp()
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(this).startSmsUserConsent(null)
            .addOnSuccessListener { /* SMS consent started */ }
            .addOnFailureListener { /* Silent fail */ }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().apply {
            smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent) {
                        consentLauncher.launch(intent) // ✅ Activación con nueva API
                    }

                    override fun onFailure() { /* Optional logging */
                    }
                }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        val flags =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) RECEIVER_EXPORTED else 0
        registerReceiver(smsBroadcastReceiver, intentFilter, flags)
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