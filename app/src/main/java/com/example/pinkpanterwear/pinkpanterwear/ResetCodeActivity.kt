package com.example.slickkwear

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pinkpanterwear.R
import com.example.slickkwear.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.Random
import java.util.regex.Pattern

class ResetCodeActivity : AppCompatActivity() {
    private var verify1: TextInputLayout? = null
    private var verify2: TextInputLayout? = null
    private var verify3: TextInputLayout? = null
    private var verify4: TextInputLayout? = null
    private var verificationButton: Button? = null
    private var resend_OTP: TextView? = null
    private var resend_otp_counter: TextView? = null
    private var phone_number_reset_code_sent: TextView? = null
    private var userID: String? = null

    private var userRef: FirebaseFirestore? = null


    //    private static final int READ_SMS = 200;
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_code)

        userRef = FirebaseFirestore.getInstance()

        userID = intent.getStringExtra("userID")

        verify1 = findViewById<TextInputLayout>(R.id.verify_1)
        verify2 = findViewById<TextInputLayout>(R.id.verify_2)
        verify3 = findViewById<TextInputLayout>(R.id.verify_3)
        verify4 = findViewById<TextInputLayout>(R.id.verify_4)

        verificationButton = findViewById<Button>(R.id.reset_code_btn)

        resend_OTP = findViewById<TextView>(R.id.resend_OTP)
        resend_otp_counter = findViewById<TextView>(R.id.resend_OTP_counter)
        phone_number_reset_code_sent = findViewById<TextView>(R.id.phone_number_reset_code_sent)

        phone_number_reset_code_sent.setText("+$userID")

        startSmsUserConsent()

        liveDataValidate()

        verificationButton.setOnClickListener(
            View.OnClickListener { validateDataOnBtnClick() }
        )

        resendCodeCountDownTimer()

        resend_OTP.setOnClickListener(
            View.OnClickListener { saveResetCode() }
        )
    }

    private fun resendCodeCountDownTimer() {
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resend_otp_counter!!.text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {
//                resend_otp_counter1.setText("done!");
                resend_otp_counter!!.visibility = View.GONE
                resend_OTP!!.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun saveResetCode() {
        val rand = Random()
        val OTP = String.format("%04d", rand.nextInt(10000))

        userRef!!.collection("Users").document(userID!!).update("UserOTP", OTP)
            .addOnSuccessListener { resend_OTP(OTP) }
    }

    private fun resend_OTP(OTP: String) {
        Thread {
            val baseUrl = "https://mysms.celcomafrica.com/api/services/sendsms/"
            val partnerId = 2881 // your ID here
            val apiKey = "d72d2587d85c517381ca0daa34ff4c9c" // your API key
            val shortCode = "CELCOM_SMS" // sender ID here e.g INFOTEXT, Celcom, e.t.c

            val gateway = SmsGateway(baseUrl, partnerId, apiKey, shortCode)

            val strings = arrayOf(userID)
            val user_msg =
                "$OTP: is your Password Reset Code for Slickk Wear App."
            try {
                val res = gateway.sendBulkSms(user_msg, strings)
                println(res)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()

        startSmsUserConsent()

        resend_OTP!!.visibility = View.GONE
        resend_otp_counter!!.visibility = View.VISIBLE

        Toast.makeText(this, "Reset Code has been sent!", Toast.LENGTH_SHORT).show()
        resendCodeCountDownTimer()
    }

    private fun liveDataValidate() {
        verify1!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.length == 1) {
                        verify2!!.editText!!.requestFocus()
                        verify1!!.error = null
                    } else {
                        verify1!!.editText!!.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        verify2!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.length == 1) {
                        verify3!!.editText!!.requestFocus()
                        verify2!!.error = null
                    } else {
                        verify1!!.editText!!.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        verify3!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.length == 1) {
                        verify4!!.editText!!.requestFocus()
                        verify3!!.error = null
                    } else {
                        verify2!!.editText!!.requestFocus()
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        verify4!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.length == 0) {
                        verify3!!.editText!!.requestFocus()
                    } else {
                        verify4!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )
    }

    private fun validateDataOnBtnClick() {
        val v1 = verify1!!.editText!!.text.toString()
        val v2 = verify2!!.editText!!.text.toString()
        val v3 = verify3!!.editText!!.text.toString()
        val v4 = verify4!!.editText!!.text.toString()

        if (TextUtils.isEmpty(v1)) {
            verify1!!.error = "Required!"
        }
        if (TextUtils.isEmpty(v2)) {
            verify2!!.error = "Required!"
        }
        if (TextUtils.isEmpty(v3)) {
            verify3!!.error = "Required!"
        }
        if (TextUtils.isEmpty(v4)) {
            verify4!!.error = "Required!"
        }

        if (!TextUtils.isEmpty(v1) && !TextUtils.isEmpty(v2) && !TextUtils.isEmpty(v3) && !TextUtils.isEmpty(
                v4
            )
        ) {
            val OTP_Merged = v1 + v2 + v3 + v4

            userRef!!.collection("Users").document(userID!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        if (OTP_Merged == documentSnapshot.getString("UserOTP")) {
                            val intent = Intent(
                                applicationContext,
                                ResetPasswordActivity::class.java
                            )
                            intent.putExtra("userID", userID)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@ResetCodeActivity,
                                "Wrong Reset Code !!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            //                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
        }.addOnFailureListener {
            //                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)

                //                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message!!)
            }
        }
    }

    private fun getOtpFromMessage(message: String) {
        val pattern = Pattern.compile("(|^)\\d{4}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            val OTP = matcher.group(0)

            val n1 = OTP!![0]
            val n2 = OTP[1]
            val n3 = OTP[2]
            val n4 = OTP[3]

            verify1!!.editText!!.setText(n1.toString() + "")
            verify2!!.editText!!.setText(n2.toString() + "")
            verify3!!.editText!!.setText(n3.toString() + "")
            verify4!!.editText!!.setText(n4.toString() + "")

            //            Toast.makeText(this, ""+n1+","+n2+","+n3+","+n4, Toast.LENGTH_SHORT).show();
            validateDataOnBtnClick()
        }
    }


    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent!!, REQ_USER_CONSENT)
                }

                override fun onFailure() {
                }
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        ContextCompat.registerReceiver(
            this,
            smsBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    companion object {
        private const val REQ_USER_CONSENT = 200
    }
}