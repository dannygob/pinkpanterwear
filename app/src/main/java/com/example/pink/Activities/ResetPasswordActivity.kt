package com.example.pink.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ResetPasswordActivity : AppCompatActivity() {

    private var resetPasswordLayout: TextInputLayout? = null
    private var resetConfirmPasswordLayout: TextInputLayout? = null
    private var resetButton: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var userRef: FirebaseFirestore? = null
    private var userID: String = ""
    private var generatedPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        // Inicializar vistas
        resetPasswordLayout = findViewById(R.id.reset_password)
        resetConfirmPasswordLayout = findViewById(R.id.reset_confirm_password)
        resetButton = findViewById(R.id.reset_password_btn)
        loadingBar = ProgressDialog(this)

        // Firebase
        userRef = FirebaseFirestore.getInstance()
        userID = intent.getStringExtra("userID").orEmpty()

        // Validación en tiempo real
        setupLiveValidation()

        // Acción de botón
        resetButton?.setOnClickListener {
            validateInputsAndSave()
        }
    }

    private fun setupLiveValidation() {
        resetPasswordLayout?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.isNullOrEmpty() -> resetPasswordLayout?.error = "Password cannot be empty!"
                    s.length < 8 -> resetPasswordLayout?.error = "Minimum of 8 characters required"
                    !isValidPassword(s) -> resetPasswordLayout?.error =
                        "Must contain numbers and letters"

                    else -> resetPasswordLayout?.error = null
                }
            }
        })

        resetConfirmPasswordLayout?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = resetPasswordLayout?.editText?.text.toString()
                when {
                    s.isNullOrEmpty() -> resetConfirmPasswordLayout?.error =
                        "Password cannot be empty!"

                    s.toString() != password -> resetConfirmPasswordLayout?.error =
                        "Passwords do not match!"

                    else -> resetConfirmPasswordLayout?.error = null
                }
            }
        })
    }

    private fun validateInputsAndSave() {
        val password = resetPasswordLayout?.editText?.text.toString()
        val confirmPassword = resetConfirmPasswordLayout?.editText?.text.toString()

        when {
            password.isEmpty() -> resetPasswordLayout?.error = "Password cannot be empty!"
            password.length < 8 -> resetPasswordLayout?.error = "Minimum of 8 characters required"
            !isValidPassword(password) -> resetPasswordLayout?.error =
                "Must contain numbers and letters"

            confirmPassword.isEmpty() -> resetConfirmPasswordLayout?.error =
                "Password cannot be empty!"

            confirmPassword != password -> resetConfirmPasswordLayout?.error =
                "Passwords do not match!"

            else -> {
                loadingBar?.apply {
                    setTitle("Resetting Password")
                    setMessage("Please wait...")
                    setCanceledOnTouchOutside(false)
                    show()
                }

                saveNewPassword(password)
            }
        }
    }

    private fun saveNewPassword(password: String) {
        hashPassword(password)

        userRef?.collection("Users")
            ?.document(userID)
            ?.update("UserPassword", generatedPassword)
            ?.addOnSuccessListener(OnSuccessListener<Void?> {
                loadingBar?.dismiss()
                Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
    }

    private fun hashPassword(password: String) {
        try {
            val md = MessageDigest.getInstance("MD5")
            val digestBytes = md.digest(password.toByteArray())
            val sb = StringBuilder()
            for (b in digestBytes) {
                sb.append(((b.toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Toast.makeText(this, "Hashing failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private fun isValidPassword(password: CharSequence): Boolean {
            return password.contains(Regex("[0-9]")) &&
                    (password.contains(Regex("[A-Z]")) || password.contains(Regex("[a-z]")))
        }
    }
}
