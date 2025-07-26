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
    private lateinit var userRef: FirebaseFirestore

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
        userRef = FirebaseFirestore.getInstance()

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        validateLiveUserData()

        loginBtn.setOnClickListener {
            validateOnBtnClick()
        }
    }

    private fun addInputValidation() {
        loginPhone.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val regex = Regex("^7[0-9]{8}$")
                    loginPhone.error = when {
                        it.isEmpty() -> getString(R.string.empty_number)
                        !regex.matches(it) -> getString(R.string.number_format)
                        else -> null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loginPassword.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginPassword.error =
                    if (s.isNullOrEmpty()) getString(R.string.empty_password) else null
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateOnBtnClick() {
        val phone = loginPhone.editText?.text.toString()
        val password = loginPassword.editText?.text.toString()
        val userID = "254$phone"

        if (phone.isEmpty()) {
            loginPhone.error = getString(R.string.empty_number)
            return
        }
        if (!Regex("^7[0-9]{8}$").matches(phone)) {
            loginPhone.error = getString(R.string.number_format)
            return
        }
        if (password.isEmpty()) {
            loginPassword.error = getString(R.string.empty_password)
            return
        }

        passWordHash(password)

        loadingBar.setTitle(getString(R.string.login_title))
        loadingBar.setMessage(getString(R.string.please_wait))
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        userRef.collection("Users").document(userID).get()
            .addOnSuccessListener { documentSnapshot ->
                loadingBar.dismiss()
                if (documentSnapshot.exists()) {
                    val firestorePhone = documentSnapshot.getString("UserPhoneNumber")
                    val firestorePassword = documentSnapshot.getString("UserPassword")
                    val isVerified = documentSnapshot.getString("UserVerified") == "true"

                    if (firestorePhone != userID) {
                        Toast.makeText(
                            this,
                            getString(R.string.number_mismatch),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnSuccessListener
                    }
                    if (generatedPassword != firestorePassword) {
                        Toast.makeText(
                            this,
                            getString(R.string.incorrect_password),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnSuccessListener
                    }
                    if (!isVerified) {
                        startActivity(Intent(this, PhoneVerificationActivity::class.java))
                        return@addOnSuccessListener
                    }

                    loginUser(userID, password)
                } else {
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
                    getString(R.string.login_error),
                    Toast.LENGTH_SHORT
                ).show()
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

    private fun passWordHash(password: String) {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            val sb = StringBuilder()
            for (b in bytes) {
                sb.append(((b.toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            generatedPassword = password // fallback
        }
    }
}
