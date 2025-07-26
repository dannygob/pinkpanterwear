package com.example.pink.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var forgotPasswordPhone: TextInputLayout
    private lateinit var forgotPasswordButton: Button
    private lateinit var loadingBar: ProgressDialog
    private lateinit var userRef: FirebaseFirestore

    private var userID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotPasswordPhone = findViewById(R.id.forgot_password_phone)
        forgotPasswordButton = findViewById(R.id.forgot_password_btn)
        loadingBar = ProgressDialog(this)
        userRef = FirebaseFirestore.getInstance()

        addPhoneNumberValidation()

        forgotPasswordButton.setOnClickListener {
            validateDataOnBtnClick()
        }
    }

    private fun addPhoneNumberValidation() {
        forgotPasswordPhone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val number = s.toString()
                when {
                    number.isEmpty() -> forgotPasswordPhone.error = getString(R.string.empty_number)
                    number.startsWith("0") && number.length == 10 -> forgotPasswordPhone.error =
                        getString(R.string.number_format)

                    number.length != 9 -> forgotPasswordPhone.error =
                        getString(R.string.number_format)
                    else -> forgotPasswordPhone.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateDataOnBtnClick() {
        val phone = forgotPasswordPhone.editText?.text.toString()

        if (TextUtils.isEmpty(phone)) {
            forgotPasswordPhone.error = getString(R.string.empty_phone_number)
            return
        } else if (phone.startsWith("0") || phone.length != 9) {
            forgotPasswordPhone.error = getString(R.string.number_format)
            return
        }

        userID = "254$phone"

        loadingBar.setTitle(getString(R.string.sending_reset_code_title))
        loadingBar.setMessage(getString(R.string.please_wait))
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { snapshot: DocumentSnapshot ->
                if (snapshot.exists()) {
                    saveResetCode(userID)
                } else {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this,
                        getString(R.string.user_does_not_exist, userID),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                loadingBar.dismiss()
                Toast.makeText(
                    this,
                    getString(R.string.error_accessing_user_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveResetCode(userID: String) {
        val otp = "%04d".format(Random().nextInt(10000))

        userRef.collection("Users").document(userID).update("UserOTP", otp)
            .addOnSuccessListener {
                sendResetCode(userID, otp)
            }
            .addOnFailureListener {
                loadingBar.dismiss()
                Toast.makeText(
                    this,
                    getString(R.string.could_not_save_reset_code),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun sendResetCode(userID: String, otp: String) {
        Thread {
            // Aquí podrías integrar tu pasarela SMS
            // Ejemplo: SmsGateway.sendBulkSms(...)
            // Por ahora está simulado como comentario
        }.start()

        loadingBar.dismiss()

        val intent = Intent(applicationContext, ResetCodeActivity::class.java)
        intent.putExtra("userID", userID)
        startActivity(intent)
    }
}
