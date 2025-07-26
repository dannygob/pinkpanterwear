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
import com.example.pink.R
import com.example.pink.fragment.SmsGateway
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerName: TextInputLayout
    private lateinit var registerPhone: TextInputLayout
    private lateinit var registerPassword: TextInputLayout
    private lateinit var registerConfirmPassword: TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var loadingBar: ProgressDialog

    private val userRef = FirebaseFirestore.getInstance()
    private var hashedPassword: String? = null
    private lateinit var userID: String
    private var saveCurrentDate = ""
    private var saveCurrentTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.register_name)
        registerPhone = findViewById(R.id.register_phone)
        registerPassword = findViewById(R.id.register_password)
        registerConfirmPassword = findViewById(R.id.register_confirm_password)
        registerButton = findViewById(R.id.register_button)
        loginLink = findViewById(R.id.register_login_link)
        loadingBar = ProgressDialog(this)

        setupFieldValidation()

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registerButton.setOnClickListener {
            validateRegistrationData()
        }
    }

    private fun setupFieldValidation() {
        registerName.editText?.addTextChangedListener(fieldWatcher(registerName) {
            it.isBlank() to getString(R.string.empty_name)
        })

        registerPhone.editText?.addTextChangedListener(fieldWatcher(registerPhone) {
            when {
                it.isBlank() -> true to getString(R.string.empty_phone_number)
                it.startsWith("0") || it.length != 9 -> true to getString(R.string.number_format)
                else -> false to null
            }
        })

        registerPassword.editText?.addTextChangedListener(fieldWatcher(registerPassword) {
            when {
                it.isBlank() -> true to getString(R.string.empty_password)
                it.length < 8 -> true to getString(R.string.password_min_length)
                !isValidPassword(it) -> true to getString(R.string.password_invalid)
                else -> false to null
            }
        })

        registerConfirmPassword.editText?.addTextChangedListener(
            fieldWatcher(
                registerConfirmPassword
            ) {
                val password = registerPassword.editText?.text.toString()
                when {
                    it.isBlank() -> true to getString(R.string.confirm_password_required)
                    it != password -> true to getString(R.string.passwords_do_not_match)
                    else -> false to null
                }
            })
    }

    private fun fieldWatcher(
        field: TextInputLayout,
        validate: (String) -> Pair<Boolean, String?>,
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val (hasError, msg) = validate(s?.toString().orEmpty())
                field.error = if (hasError) msg else null
            }
        }
    }

    private fun validateRegistrationData() {
        val name = registerName.editText?.text.toString().trim()
        val phone = registerPhone.editText?.text.toString().trim()
        val password = registerPassword.editText?.text.toString()
        val confirmPassword = registerConfirmPassword.editText?.text.toString()

        when {
            name.isEmpty() -> registerName.error = getString(R.string.empty_name)
            phone.isEmpty() -> registerPhone.error = getString(R.string.empty_phone_number)
            phone.startsWith("0") || phone.length != 9 -> registerPhone.error =
                getString(R.string.number_format)

            password.isEmpty() -> registerPassword.error = getString(R.string.empty_password)
            password.length < 8 -> registerPassword.error = getString(R.string.password_min_length)
            !isValidPassword(password) -> registerPassword.error =
                getString(R.string.password_invalid)

            confirmPassword != password -> registerConfirmPassword.error =
                getString(R.string.passwords_do_not_match)
            else -> {
                loadingBar.apply {
                    setTitle(getString(R.string.creating_account_title))
                    setMessage(getString(R.string.please_wait))
                    setCanceledOnTouchOutside(false)
                    show()
                }
                registerUser(name, phone, password)
            }
        }
    }

    private fun registerUser(name: String, phone: String, password: String) {
        val phoneID = "254$phone"
        userRef.collection("Users").document(phoneID).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this,
                        getString(R.string.user_already_exists, phoneID),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveUserData(phoneID, name, password)
                }
            }
    }

    private fun saveUserData(phoneID: String, name: String, password: String) {
        val now = Calendar.getInstance()
        saveCurrentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(now.time)
        saveCurrentTime = SimpleDateFormat("HHmmss", Locale.getDefault()).format(now.time)
        userID = "$saveCurrentDate$saveCurrentTime${(100_000..999_999).random()}"
        val otp = String.format("%04d", Random().nextInt(10000))
        hashedPassword = hashPassword(password)

        val userMap = mapOf(
            "UserID" to userID,
            "UserName" to name,
            "UserPhoneNumber" to phoneID,
            "UserPassword" to hashedPassword,
            "UserOTP" to otp,
            "UserStatus" to "active",
            "UserVerified" to "false",
            "CategoryDeleted" to "false"
        )

        userRef.collection("Users").document(phoneID).set(userMap)
            .addOnSuccessListener {
                sendOtp(phoneID, otp)
                loadingBar.dismiss()
                Intent(this, PhoneVerificationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("userID", phoneID) // Pass phoneID as userID for consistency
                    startActivity(this)
                }
            }
            .addOnFailureListener {
                loadingBar.dismiss()
                Toast.makeText(this, getString(R.string.user_not_created), Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun sendOtp(phone: String, otp: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gateway = SmsGateway(
                    baseUrl = "https://mysms.celcomafrica.com/api/services/sendsms/",
                    partnerId = 2881,
                    apiKey = "d72d2587d85c517381ca0daa34ff4c9c",
                    shortCode = "CELCOM_SMS"
                )
                val message = "$otp: is your Verification Code for Slickk Wear App."
                val response = gateway.sendBulkSms(message, arrayOf(phone))
                println("SMS response: $response")
            } catch (e: Exception) {
                println("SMS send failed: ${e.message}")
            }
        }
    }

    private fun hashPassword(password: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val bytes = md.digest(password.toByteArray())
            bytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        fun isValidPassword(text: String): Boolean {
            val number = Regex(".*[0-9].*")
            val upper = Regex(".*[A-Z].*")
            val lower = Regex(".*[a-z].*")
            return number.containsMatchIn(text) && (upper.containsMatchIn(text) || lower.containsMatchIn(
                text
            ))
        }
    }
}
