package com.example.pink.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.MainActivity
import com.example.pink.R
import com.example.pink.prevalent.Prevalent
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import io.paperdb.Paper
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    private lateinit var registerLink: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var loginPhone: TextInputLayout
    private lateinit var loginPassword: TextInputLayout
    private lateinit var rememberMeCheckbox: MaterialCheckBox
    private lateinit var loginBtn: Button
    private lateinit var loadingBar: ProgressDialog
    private val userRef = FirebaseFirestore.getInstance()

    private var generatedPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPhone = findViewById(R.id.login_phone)
        loginPassword = findViewById(R.id.login_password)
        loginBtn = findViewById(R.id.login_button)
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox)
        forgotPassword = findViewById(R.id.forgot_password)
        registerLink = findViewById(R.id.register_link)

        loadingBar = ProgressDialog(this)
        Paper.init(this)

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        setupInputValidation()

        loginBtn.setOnClickListener {
            validateCredentials()
        }
    }

    private fun setupInputValidation() {
        loginPhone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val regex = Regex("^7\\d{8}$")
                loginPhone.error = when {
                    s.isNullOrBlank() -> getString(R.string.empty_number)
                    !regex.matches(s) -> getString(R.string.number_format)
                    else -> null
                }
            }
        })

        loginPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginPassword.error =
                    if (s.isNullOrBlank()) getString(R.string.empty_password) else null
            }
        })
    }

    private fun validateCredentials() {
        val phone = loginPhone.editText?.text.toString()
        val password = loginPassword.editText?.text.toString()
        val userID = "254$phone"

        val regex = Regex("^7\\d{8}$")

        when {
            phone.isBlank() -> {
                loginPhone.error = getString(R.string.empty_number)
                return
            }

            !regex.matches(phone) -> {
                loginPhone.error = getString(R.string.number_format)
                return
            }

            password.isBlank() -> {
                loginPassword.error = getString(R.string.empty_password)
                return
            }
        }

        hashPassword(password)

        loadingBar.setTitle(getString(R.string.login_title))
        loadingBar.setMessage(getString(R.string.please_wait))
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { doc ->
                loadingBar.dismiss()
                if (!doc.exists()) {
                    showToast(getString(R.string.user_does_not_exist, userID))
                    return@addOnSuccessListener
                }

                val firestorePhone = doc.getString("UserPhoneNumber")
                val firestorePassword = doc.getString("UserPassword")
                val isVerified = doc.getString("UserVerified") == "true"

                when {
                    firestorePhone != userID -> showToast(getString(R.string.number_mismatch))
                    generatedPassword != firestorePassword -> showToast(getString(R.string.incorrect_password))
                    !isVerified -> startActivity(
                        Intent(
                            this,
                            PhoneVerificationActivity::class.java
                        )
                    )

                    else -> loginUser(userID, password)
                }
            }
            .addOnFailureListener {
                loadingBar.dismiss()
                showToast(getString(R.string.login_error))
            }
    }

    private fun loginUser(userID: String?, password: String?) {
        Paper.book()
            .write(Prevalent.UserRememberMe, if (rememberMeCheckbox.isChecked) "true" else "false")
        Paper.book().write(Prevalent.UserLoggedIn, "true")
        userID?.let { Paper.book().write(Prevalent.UserPhoneKey, it) }
        password?.let { Paper.book().write(Prevalent.UserPasswordKey, it) }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun hashPassword(password: String) {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            generatedPassword = bytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            generatedPassword = password // fallback
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}