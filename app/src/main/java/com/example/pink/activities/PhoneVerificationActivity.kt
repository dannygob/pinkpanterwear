package com.example.pink.activities

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pink.R
import com.example.pink.fragment.SmsBroadcastReceiver
import com.example.pink.fragment.SmsGateway
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import java.util.regex.Pattern

class PhoneVerificationActivity : AppCompatActivity() {

    private lateinit var verify1: TextInputLayout
    private lateinit var verify2: TextInputLayout
    private lateinit var verify3: TextInputLayout
    private lateinit var verify4: TextInputLayout
    private lateinit var verificationButton: Button
    private lateinit var resendOTPButton: TextView
    private lateinit var resendOtpCounter: TextView
    private lateinit var codeSentText: TextView

    private var smsBroadcastReceiver: SmsBroadcastReceiver? = null
    private lateinit var userID: String
    private val userRef = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth

    companion object {
        private const val READ_SMS = 100
        private const val REQ_USER_CONSENT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        verify1 = findViewById(R.id.verify_1)
        verify2 = findViewById(R.id.verify_2)
        verify3 = findViewById(R.id.verify_3)
        verify4 = findViewById(R.id.verify_4)
        verificationButton = findViewById(R.id.verification_button)
        resendOTPButton = findViewById(R.id.resend_OTP)
        resendOtpCounter = findViewById(R.id.resend_OTP_counter)
        codeSentText = findViewById(R.id.phone_number_verify_code_sent)

        userID = intent.getStringExtra("userID") ?: ""
        codeSentText.text = "+$userID"

        mAuth = FirebaseAuth.getInstance()

        checkSmsPermission()
        startSmsUserConsent()
        registerBroadcastReceiver()
        setupInputFields()
        startResendCounter()

        verificationButton.setOnClickListener { submitVerificationCode() }
        resendOTPButton.setOnClickListener { resendOTP() }
    }

    private fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), READ_SMS)
        }
    }

    private fun resendOTP() {
        val otp = String.format("%04d", Random().nextInt(10000))

        userRef.collection("Users").document(userID)
            .update("UserOTP", otp)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val gateway = SmsGateway(
                            baseUrl = "https://mysms.celcomafrica.com/api/services/sendsms/",
                            partnerId = 2881,
                            apiKey = "d72d2587d85c517381ca0daa34ff4c9c",
                            shortCode = "CELCOM_SMS"
                        )
                        val message = "$otp: is your Verification Code for Slickk Wear App."
                        gateway.sendBulkSms(message, arrayOf(userID))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                startSmsUserConsent()
                startResendCounter()
                Toast.makeText(this, getString(R.string.otp_resent), Toast.LENGTH_SHORT).show()
            }
    }

    private fun startResendCounter() {
        resendOTPButton.visibility = View.GONE
        resendOtpCounter.visibility = View.VISIBLE

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendOtpCounter.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                resendOtpCounter.visibility = View.GONE
                resendOTPButton.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun startSmsUserConsent() {
        val client: SmsRetrieverClient = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.waiting_for_sms), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.failed_to_start_sms_consent),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().apply {
            smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT)
                    }

                    override fun onFailure() {
                        Toast.makeText(
                            this@PhoneVerificationActivity,
                            getString(R.string.sms_capture_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        val receiverFlags =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
                RECEIVER_EXPORTED else 0

        registerReceiver(smsBroadcastReceiver, intentFilter, receiverFlags)
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        smsBroadcastReceiver?.let {
            unregisterReceiver(it)
            smsBroadcastReceiver = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT && resultCode == RESULT_OK && data != null) {
            val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let { extractOtpAndAutoFill(it) }
        }
    }

    private fun extractOtpAndAutoFill(message: String) {
        val pattern = Pattern.compile("\\d{4}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            val otp = matcher.group(0)
            verify1.editText?.setText("${otp[0]}")
            verify2.editText?.setText("${otp[1]}")
            verify3.editText?.setText("${otp[2]}")
            verify4.editText?.setText("${otp[3]}")
            submitVerificationCode()
        }
    }

    private fun setupInputFields() {
        val fields = listOf(verify1, verify2, verify3, verify4)

        fields.forEachIndexed { index, field ->
            field.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        field.error = null
                        if (index < fields.lastIndex) {
                            fields[index + 1].editText?.requestFocus()
                        } else {
                            submitVerificationCode()
                        }
                    } else if (s?.isEmpty() == true && index > 0) {
                        fields[index - 1].editText?.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun submitVerificationCode() {
        val otpDigits = listOf(
            verify1.editText?.text?.toString()?.trim(),
            verify2.editText?.text?.toString()?.trim(),
            verify3.editText?.text?.toString()?.trim(),
            verify4.editText?.text?.toString()?.trim()
        )

        if (otpDigits.any { it.isNullOrEmpty() }) {
            val fields = listOf(verify1, verify2, verify3, verify4)
            otpDigits.forEachIndexed { index, digit ->
                fields[index].error =
                    if (digit.isNullOrEmpty()) getString(R.string.required) else null
            }
            return
        }

        val otpMerged = otpDigits.joinToString("")

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val storedOtp = doc.getString("UserOTP")
                    if (otpMerged == storedOtp) {
                        userRef.collection("Users").document(userID)
                            .update("UserVerified", "true")
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    getString(R.string.verification_successful),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    getString(R.string.verification_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.incorrect_otp),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.user_record_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.failed_to_verify), Toast.LENGTH_SHORT)
                    .show()
            }
    }
}

