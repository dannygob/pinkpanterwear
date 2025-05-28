package com.example.slickkwear

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pinkpanterwear.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class ResetPasswordActivity : AppCompatActivity() {
    private var reset_password: TextInputLayout? = null
    private var reset_confirm_password: TextInputLayout? = null
    private var reset_btn: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var userRef: FirebaseFirestore? = null
    private var userID: String? = null
    private var generatedPassword: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        reset_password = findViewById<View>(R.id.reset_password) as TextInputLayout
        reset_confirm_password = findViewById<View>(R.id.reset_confirm_password) as TextInputLayout
        loadingBar = ProgressDialog(this)

        reset_btn = findViewById<View>(R.id.reset_password_btn) as Button

        liveDataValidate()

        reset_btn!!.setOnClickListener { validateDataOnBtnClick() }

        userRef = FirebaseFirestore.getInstance()
        userID = intent.getStringExtra("userID")
    }

    private fun liveDataValidate() {
        reset_password!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (TextUtils.isEmpty(charSequence)) {
                        reset_password!!.error = "Password cannot be empty!"
                    } else if (charSequence.toString().length < 8) {
                        reset_password!!.error = "Minimum of 8 characters required"
                    } else if (!isValidPassword(charSequence)) {
                        reset_password!!.error = "Must contain both numbers and letters"
                    } else {
                        reset_password!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        reset_confirm_password!!.editText!!.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val password = reset_password!!.editText!!.text.toString()

                    if (TextUtils.isEmpty(charSequence)) {
                        reset_confirm_password!!.error = "Password cannot be empty!"
                    } else if (charSequence.toString() != password) {
                        reset_confirm_password!!.error = "Passwords do not match!"
                    } else {
                        reset_confirm_password!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )
    }

    private fun validateDataOnBtnClick() {
        val password = reset_password!!.editText!!.text.toString()
        val confirm_password = reset_confirm_password!!.editText!!.text.toString()

        if (TextUtils.isEmpty(password)) {
            reset_password!!.error = "Password cannot be empty!"
        } else if (password.length < 8) {
            reset_password!!.error = "Minimum of 8 characters required"
        } else if (!isValidPassword(password)) {
            reset_password!!.error = "Must contain both numbers and letters"
        } else if (TextUtils.isEmpty(confirm_password)) {
            reset_confirm_password!!.error = "Password cannot be empty!"
        } else if (confirm_password != password) {
            reset_confirm_password!!.error = "Passwords do not match!"
        } else {
            loadingBar!!.setTitle("Resetting Password")
            loadingBar!!.setMessage("Please wait...")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()

            saveNewPassword(password, confirm_password)
        }
    }

    private fun saveNewPassword(password: String, confirm_password: String) {
        passWordHash(password)

        userRef!!.collection("Users").document(userID!!).update("UserPassword", generatedPassword)
            .addOnSuccessListener {
                loadingBar!!.dismiss()
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Password reset successful!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
    }

    private fun passWordHash(password: String) {
        val passwordToHash = password
        generatedPassword = null
        try {
            // Create MessageDigest instance for MD5
            val md = MessageDigest.getInstance("MD5")
            //Add password bytes to digest
            md.update(passwordToHash.toByteArray())
            //Get the hash's bytes
            val bytes = md.digest()
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(((bytes[i].toInt() and 0xff) + 0x100).toString(16).substring(1))
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    companion object {
        private fun isValidPassword(charSequence: CharSequence): Boolean {
            val n = ".*[0-9].*"
            val caps_l = ".*[A-Z].*"
            val small_l = ".*[a-z].*"
            return charSequence.toString().matches(n.toRegex()) && (charSequence.toString()
                .matches(caps_l.toRegex()) || charSequence.toString().matches(small_l.toRegex()))
        }
    }
}