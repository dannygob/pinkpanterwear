package com.example.slickkwear

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pinkpanterwear.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Random

class ForgotPasswordActivity : AppCompatActivity() {
    private var forgot_password_phone: TextInputLayout? = null
    private var forgot_password_button: Button? = null
    private var loadingBar: ProgressDialog? = null

    private var userID: String? = null

    private var userRef: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgot_password_phone = findViewById<View>(R.id.forgot_password_phone) as TextInputLayout
        forgot_password_button = findViewById<View>(R.id.forgot_password_btn) as Button

        loadingBar = ProgressDialog(this)

        userRef = FirebaseFirestore.getInstance()

        validateLiveData()
        forgot_password_button!!.setOnClickListener { validateDataOnBtnClick() }
    }


    private fun validateLiveData() {
        forgot_password_phone!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (TextUtils.isEmpty(charSequence)) {
                        forgot_password_phone!!.error = "Number cannot be empty!"
                    } else if ((charSequence.toString()
                            .startsWith("0") && charSequence.toString().length == 10) || charSequence.toString().length > 9 || charSequence.toString().length < 9
                    ) {
                        forgot_password_phone!!.error = "Number format: 705..."
                    } else {
                        forgot_password_phone!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )
    }


    private fun validateDataOnBtnClick() {
        val phone = forgot_password_phone!!.editText!!.text.toString()

        userID = "254$phone"

        if (TextUtils.isEmpty(phone)) {
            forgot_password_phone!!.error = "Phone Number cannot be empty!"
        } else if (phone.startsWith("0") || phone.length > 9 || phone.length < 9) {
            forgot_password_phone!!.error = "Phone Number format: 705..."
        } else {
            loadingBar!!.setTitle("Sending reset code")
            loadingBar!!.setMessage("Please wait ...")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()

            userRef!!.collection("Users").document(userID!!).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        saveResetCode(userID!!)
                    } else {
                        loadingBar!!.dismiss()
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "User +$userID does not exist!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun saveResetCode(userID: String) {
        val rand = Random()
        val OTP = String.format("%04d", rand.nextInt(10000))

        userRef!!.collection("Users").document(userID).update("UserOTP", OTP)
            .addOnSuccessListener {
                sendResetCode(
                    userID,
                    OTP
                )
            }
    }

    private fun sendResetCode(userID: String, OTP: String) {
        Thread {
            // Send Alert
            //                String baseUrl = "";
            //                int partnerId = ;
            //                String apiKey = "";
            //                String shortCode = "";
            //
            //                SmsGateway gateway = new SmsGateway(baseUrl, partnerId, apiKey, shortCode);
            //
            //                String[] strings = {userID};
            //                String user_msg = OTP + ": is your Password Reset Code for Slickk Wear App.";
            //
            //                try {
            //                    String res = gateway.sendBulkSms(user_msg, strings);
            //                    System.out.println(res);
            //                } catch (IOException e) {
            //                    e.printStackTrace();
            //                }
        }.start()

        loadingBar!!.dismiss()
        val intent = Intent(
            applicationContext,
            ResetCodeActivity::class.java
        )
        intent.putExtra("userID", userID)
        startActivity(intent)
    }
}