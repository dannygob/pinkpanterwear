package com.example.slickkwear

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pinkpanterwear.R
import com.example.slickkwear.Prevalent.Prevalent
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private var register_link: TextView? = null
    private var forgotPassword: TextView? = null
    private var loginPhone: TextInputLayout? = null
    private var loginPassword: TextInputLayout? = null
    private var remember_me_checkbox: MaterialCheckBox? = null
    private var loginBtn: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var userRef: FirebaseFirestore? = null
    private var generatedPassword: String? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPhone = findViewById<TextInputLayout>(R.id.login_phone)
        loginPassword = findViewById<TextInputLayout>(R.id.login_password)
        loginBtn = findViewById<Button>(R.id.login_button)
        remember_me_checkbox = findViewById<MaterialCheckBox>(R.id.remember_me_checkbox)
        forgotPassword = findViewById<TextView>(R.id.forgot_password)

        loadingBar = ProgressDialog(this)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        register_link = findViewById<TextView>(R.id.register_link)
        register_link.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
                startActivity(intent)
            }
        )

        forgotPassword.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(
                    applicationContext,
                    ForgotPasswordActivity::class.java
                )
                startActivity(intent)
            }
        )

        userRef = FirebaseFirestore.getInstance()

        validateLiveUserData()

        loginBtn.setOnClickListener(
            View.OnClickListener { validateOnBtnClick() }
        )
    }

    private fun validateLiveUserData() {
        loginPhone!!.editText!!.addTextChangedListener(
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
                        loginPhone!!.error = "Number cannot be empty!"
                    } else if ((charSequence.toString()
                            .startsWith("0") && charSequence.toString().length == 10) || charSequence.toString().length > 9 || charSequence.toString().length < 9
                    ) {
                        loginPhone!!.error = "Number format: 705..."
                    } else {
                        loginPhone!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )

        loginPassword!!.editText!!.addTextChangedListener(
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
                        loginPassword!!.error = "Password cannot be empty!"
                    } else {
                        loginPassword!!.error = null
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            }
        )
    }

    private fun validateOnBtnClick() {
        val phone = loginPhone!!.editText!!.text.toString()
        val password = loginPassword!!.editText!!.text.toString()

        val userID = "254$phone"

        passWordHash(password)

        if (TextUtils.isEmpty(phone)) {
            loginPhone!!.error = "Phone Number cannot be empty!"
        } else if (phone.startsWith("0") || phone.length > 9 || phone.length < 9) {
            loginPhone!!.error = "Phone Number format: 705..."
        } else if (TextUtils.isEmpty(password)) {
            loginPassword!!.error = "Password cannot be empty!"
        } else {
            loadingBar!!.setTitle("Login in")
            loadingBar!!.setMessage("Please wait ...")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()


            userRef!!.collection("Users").document(userID).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        if (userID == documentSnapshot.getString("UserPhoneNumber") && generatedPassword == documentSnapshot.getString(
                                "UserPassword"
                            )
                        ) {
                            loadingBar!!.dismiss()

                            if (documentSnapshot.getString("UserVerified") == "true") {
                                loginUser(userID, password)
                            } else {
                                val intent = Intent(
                                    applicationContext,
                                    PhoneVerificationActivity::class.java
                                )
                                startActivity(intent)
                            }
                        } else {
                            loadingBar!!.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Phone number or Password is incorrect!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        loadingBar!!.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            "User +$userID does not exist!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun loginUser(userID: String, password: String) {
        if (remember_me_checkbox!!.isChecked) {
            sharedPreferences!!.edit().putBoolean(Prevalent.UserRememberMe, true).apply()
        } else {
            sharedPreferences!!.edit().putBoolean(Prevalent.UserRememberMe, false).apply()
        }
        sharedPreferences!!.edit().putBoolean(Prevalent.UserLoggedIn, true).apply()
        sharedPreferences!!.edit().putString(Prevalent.UserPhoneKey, userID).apply()
        sharedPreferences!!.edit().putString(Prevalent.UserPasswordKey, password).apply()

        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
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
}