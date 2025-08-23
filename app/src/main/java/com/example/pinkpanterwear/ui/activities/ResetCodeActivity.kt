package com.example.pinkpanterwear.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pinkpanterwear.R

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ResetCodeActivity : AppCompatActivity() {

    private lateinit var verify1: TextInputEditText
    private lateinit var verify2: TextInputEditText
    private lateinit var verify3: TextInputEditText
    private lateinit var verify4: TextInputEditText
    private lateinit var submitButton: Button
    private lateinit var phoneNumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_code)

        setupActionBar()

        verify1 = findViewById<TextInputLayout>(R.id.verify_1).editText as TextInputEditText
        verify2 = findViewById<TextInputLayout>(R.id.verify_2).editText as TextInputEditText
        verify3 = findViewById<TextInputLayout>(R.id.verify_3).editText as TextInputEditText
        verify4 = findViewById<TextInputLayout>(R.id.verify_4).editText as TextInputEditText
        submitButton = findViewById(R.id.reset_code_btn)
        phoneNumberTextView = findViewById(R.id.phone_number_reset_code_sent)

        val phoneNumber = intent.getStringExtra("phone_number")
        phoneNumberTextView.text = phoneNumber

        setupOtpListeners()

        submitButton.setOnClickListener {
            verifyCode()
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_reset_code_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupOtpListeners() {
        verify1.addTextChangedListener(OtpTextWatcher(verify1, verify2))
        verify2.addTextChangedListener(OtpTextWatcher(verify2, verify3))
        verify3.addTextChangedListener(OtpTextWatcher(verify3, verify4))
        verify4.addTextChangedListener(OtpTextWatcher(verify4, null))
    }

    private fun verifyCode() {
        val code = "${verify1.text}${verify2.text}${verify3.text}${verify4.text}"
        if (code.length == 4) {
            // For now, we'll just simulate a successful verification
            // In a real app, you would send this code to your backend for verification
            Toast.makeText(this, "Code Verified Successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please enter the complete 4-digit code.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    inner class OtpTextWatcher(private val currentView: EditText, private val nextView: EditText?) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s?.length == 1) {
                nextView?.requestFocus()
            }
        }
    }
}
