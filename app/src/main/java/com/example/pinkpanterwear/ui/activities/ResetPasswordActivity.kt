package com.example.pinkpanterwear.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pinkpanterwear.R

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var newPasswordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        setupActionBar()

        newPasswordEditText =
            findViewById<TextInputLayout>(R.id.reset_password).editText as TextInputEditText
        confirmPasswordEditText =
            findViewById<TextInputLayout>(R.id.reset_confirm_password).editText as TextInputEditText
        submitButton = findViewById(R.id.reset_password_btn)

        submitButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_reset_password_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun resetPassword() {
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter both new password and confirm password.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(
                this,
                "New password and confirm password do not match.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Here you would typically send the new password to your backend for update
        // For now, we'll just simulate a successful password reset
        Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show()
        finish() // Go back to the previous activity (e.g., LoginActivity)
    }
}
