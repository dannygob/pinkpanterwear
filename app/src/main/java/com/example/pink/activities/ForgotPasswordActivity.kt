package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var forgotPasswordPhone: TextInputLayout
    private lateinit var forgotPasswordButton: Button
    private lateinit var loadingBar: ProgressBar
    private val userRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var userID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotPasswordPhone = findViewById(R.id.forgot_password_phone)
        forgotPasswordButton = findViewById(R.id.forgot_password_btn)
        loadingBar = findViewById(R.id.progress_bar)

        addPhoneNumberValidation()

        forgotPasswordButton.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun addPhoneNumberValidation() {
        forgotPasswordPhone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val number = s.toString()
                forgotPasswordPhone.error = when {
                    number.isBlank() -> getString(R.string.empty_number)
                    number.startsWith("0") && number.length == 10 -> getString(R.string.number_format)
                    number.length != 9 -> getString(R.string.number_format)
                    else -> null
                }
            }
        })
    }

    private fun validateAndSubmit() {
        val phone = forgotPasswordPhone.editText?.text.toString()

        when {
            phone.isBlank() -> {
                forgotPasswordPhone.error = getString(R.string.empty_phone_number)
                return
            }

            phone.startsWith("0") || phone.length != 9 -> {
                forgotPasswordPhone.error = getString(R.string.number_format)
                return
            }
        }

        userID = "254$phone"

        loadingBar.visibility = View.VISIBLE

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    generateAndSaveOTP(userID)
                } else {
                    loadingBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        getString(R.string.user_does_not_exist, userID),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                loadingBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.error_accessing_user_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun generateAndSaveOTP(userID: String) {
        val otp = "%04d".format(Random.nextInt(10000))

        userRef.collection("Users").document(userID).update("UserOTP", otp)
            .addOnSuccessListener {
                proceedToResetScreen(userID, otp)
            }
            .addOnFailureListener {
                loadingBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.could_not_save_reset_code),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun proceedToResetScreen(userID: String, otp: String) {
        // Simular envío SMS si integras gateway aquí
        loadingBar.visibility = View.GONE
        val intent = Intent(this, ResetCodeActivity::class.java)
        intent.putExtra("userID", userID)
        startActivity(intent)
    }
}
