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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var resetPasswordLayout: TextInputLayout
    private lateinit var resetConfirmPasswordLayout: TextInputLayout
    private lateinit var resetButton: Button
    private lateinit var loadingBar: ProgressBar
    private lateinit var userRef: FirebaseFirestore
    private lateinit var userID: String
    private var generatedPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Inicializar vistas
        resetPasswordLayout = findViewById(R.id.reset_password)
        resetConfirmPasswordLayout = findViewById(R.id.reset_confirm_password)
        resetButton = findViewById(R.id.reset_password_btn)
        loadingBar = findViewById(R.id.progress_bar)

        // Firebase
        userRef = FirebaseFirestore.getInstance()
        userID = intent.getStringExtra("userID").orEmpty()

        // Validación en tiempo real
        setupLiveValidation()

        // Acción de botón
        resetButton.setOnClickListener {
            validateInputsAndSave()
        }
    }

    private fun setupLiveValidation() {
        resetPasswordLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.isNullOrEmpty() -> resetPasswordLayout.error =
                        getString(R.string.empty_password)

                    s.length < 8 -> resetPasswordLayout.error =
                        getString(R.string.password_min_length)

                    !isValidPassword(s) -> resetPasswordLayout.error =
                        getString(R.string.password_invalid)

                    else -> resetPasswordLayout.error = null
                }
            }
        })

        resetConfirmPasswordLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = resetPasswordLayout.editText?.text.toString()
                when {
                    s.isNullOrEmpty() -> resetConfirmPasswordLayout.error =
                        getString(R.string.empty_password)

                    s.toString() != password -> resetConfirmPasswordLayout.error =
                        getString(R.string.passwords_do_not_match)

                    else -> resetConfirmPasswordLayout.error = null
                }
            }
        })
    }

    private fun validateInputsAndSave() {
        val password = resetPasswordLayout.editText?.text.toString()
        val confirmPassword = resetConfirmPasswordLayout.editText?.text.toString()

        when {
            password.isEmpty() -> resetPasswordLayout.error = getString(R.string.empty_password)
            password.length < 8 -> resetPasswordLayout.error =
                getString(R.string.password_min_length)

            !isValidPassword(password) -> resetPasswordLayout.error =
                getString(R.string.password_invalid)

            confirmPassword.isEmpty() -> resetConfirmPasswordLayout.error =
                getString(R.string.empty_password)

            confirmPassword != password -> resetConfirmPasswordLayout.error =
                getString(R.string.passwords_do_not_match)

            else -> {
                loadingBar.visibility = View.VISIBLE

                saveNewPassword(password)
            }
        }
    }

    private fun saveNewPassword(password: String) {
        hashPassword(password)

        userRef.collection("Users")
            .document(userID)
            .update("UserPassword", generatedPassword)
            .addOnSuccessListener(OnSuccessListener {
                loadingBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.password_reset_successful),
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
    }

    private fun hashPassword(password: String) {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val digestBytes = md.digest(password.toByteArray())
            val sb = StringBuilder()
            for (b in digestBytes) {
                sb.append(((b.toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Toast.makeText(this, getString(R.string.hashing_failed), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private fun isValidPassword(password: CharSequence): Boolean {
            return password.contains(Regex("[0-9]")) &&
                    (password.contains(Regex("[A-Z]")) || password.contains(Regex("[a-z]")))
        }
    }
}
