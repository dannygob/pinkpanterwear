package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
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

class LoginActivity : AppCompatActivity() {

    private lateinit var loginIdentifier: TextInputLayout
    private lateinit var loginPassword: TextInputLayout
    private lateinit var rememberMeCheckbox: MaterialCheckBox
    private lateinit var loadingBar: View

    private val userRef = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicialización de vistas
        loginIdentifier = findViewById(R.id.login_phone) // ← ID corregido según tu layout
        loginPassword = findViewById(R.id.login_password)
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox)
        loadingBar = findViewById(R.id.progress_bar)

        val loginBtn = findViewById<View>(R.id.login_button)
        val registerLink = findViewById<View>(R.id.register_link)
        val forgotPassword = findViewById<View>(R.id.forgot_password)

        Paper.init(this)

        // Navegación
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        loginBtn.setOnClickListener {
            validateCredentials()
        }
    }

    private fun validateCredentials() {
        val identifier = loginIdentifier.editText?.text?.toString()?.trim() ?: ""
        val password = loginPassword.editText?.text?.toString()?.trim() ?: ""

        if (identifier.isEmpty()) {
            loginIdentifier.error = getString(R.string.empty_identifier)
            return
        }

        if (!isValidIdentifier(identifier)) {
            loginIdentifier.error = getString(R.string.invalid_identifier_format)
            return
        }

        if (password.isEmpty()) {
            loginPassword.error = getString(R.string.empty_password)
            return
        }

        val hashedPassword = hashPassword(password)
        loadingBar.visibility = View.VISIBLE

        userRef.collection("Users").document(identifier).get()
            .addOnSuccessListener { doc ->
                loadingBar.visibility = View.GONE
                if (!doc.exists()) {
                    showToast(getString(R.string.user_does_not_exist, identifier))
                    return@addOnSuccessListener
                }

                val firestorePassword = doc.getString("UserPassword")
                if (hashedPassword != firestorePassword) {
                    showToast(getString(R.string.incorrect_password))
                    return@addOnSuccessListener
                }

                loginUser(identifier, password)
            }
            .addOnFailureListener { e ->
                loadingBar.visibility = View.GONE
                showToast("Error: ${e.message}")
            }
    }

    private fun isValidIdentifier(identifier: String): Boolean {
        return Patterns.PHONE.matcher(identifier).matches() ||
                Patterns.EMAIL_ADDRESS.matcher(identifier).matches() ||
                identifier.length >= 4
    }

    private fun loginUser(userID: String, password: String) {
        Paper.book().apply {
            write(Prevalent.UserRememberMe, rememberMeCheckbox.isChecked.toString())
            write(Prevalent.UserLoggedIn, "true")
            write(Prevalent.UserPhoneKey, userID)
            write(Prevalent.UserPasswordKey, password)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}