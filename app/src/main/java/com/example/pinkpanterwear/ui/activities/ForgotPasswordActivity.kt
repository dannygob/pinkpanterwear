package com.example.pinkpanterwear.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pinkpanterwear.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        // Add Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Add back button

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val sendCodeButton: Button = findViewById(R.id.sendCodeButton)
        val codeEditText: EditText = findViewById(R.id.codeEditText)
        val verifyCodeButton: Button = findViewById(R.id.verifyCodeButton)

        sendCodeButton.setOnClickListener {
            email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset code sent to your email", Toast.LENGTH_SHORT)
                            .show()
                        codeEditText.visibility = android.view.View.VISIBLE
                        verifyCodeButton.visibility = android.view.View.VISIBLE
                        sendCodeButton.isEnabled = false
                        emailEditText.isEnabled = false
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to send reset code. Please check your email and try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        verifyCodeButton.setOnClickListener {
            val code = codeEditText.text.toString().trim()

            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter the reset code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Navigate to ResetPasswordActivity
            val intent = Intent(this, ResetPasswordActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("code", code)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Go back when back button is pressed
        return true
    }
}
