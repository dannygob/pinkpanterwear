package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerName: TextInputLayout
    private lateinit var registerIdentifier: TextInputLayout
    private lateinit var registerPassword: TextInputLayout
    private lateinit var registerConfirmPassword: TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView
    private lateinit var loadingBar: ProgressBar

    private val userRef = FirebaseFirestore.getInstance()
    private var hashedPassword: String? = null
    private lateinit var userID: String
    private var saveCurrentDate = ""
    private var saveCurrentTime = ""
    private lateinit var registerPhone: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerName = findViewById(R.id.register_name)
        registerIdentifier = findViewById(R.id.register_identifier)
        registerPassword = findViewById(R.id.register_password)
        registerPhone = findViewById(R.id.register_phone)
        registerConfirmPassword = findViewById(R.id.register_confirm_password)
        registerButton = findViewById(R.id.register_button)
        loginLink = findViewById(R.id.register_login_link)
        loadingBar = findViewById(R.id.progress_bar)

        setupFieldValidation()

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registerButton.setOnClickListener {
            validateRegistrationData()
        }

        // Pre-fill the registration form with test data
        registerName.editText?.setText("Test User")
        registerIdentifier.editText?.setText("test@example.com")
        registerPassword.editText?.setText("Test1234")
        registerConfirmPassword.editText?.setText("Test1234")

        // Automatically submit the form
        validateRegistrationData()
    }

    private fun setupFieldValidation() {
        registerName.editText?.addTextChangedListener(fieldWatcher(registerName) {
            it.isBlank() to getString(R.string.empty_name)
        })

        registerIdentifier.editText?.addTextChangedListener(fieldWatcher(registerIdentifier) {
            when {
                it.isBlank() -> true to getString(R.string.empty_identifier)
                !android.util.Patterns.EMAIL_ADDRESS.matcher(it)
                    .matches() -> true to getString(R.string.invalid_email)
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
        val identifier = registerIdentifier.editText?.text.toString().trim()
        val password = registerPassword.editText?.text.toString()
        val confirmPassword = registerConfirmPassword.editText?.text.toString()

        when {
            name.isEmpty() -> registerName.error = getString(R.string.empty_name)
            identifier.isEmpty() -> registerIdentifier.error = getString(R.string.empty_identifier)
            password.isEmpty() -> registerPassword.error = getString(R.string.empty_password)
            password.length < 8 -> registerPassword.error = getString(R.string.password_min_length)
            !isValidPassword(password) -> registerPassword.error =
                getString(R.string.password_invalid)

            confirmPassword != password -> registerConfirmPassword.error =
                getString(R.string.passwords_do_not_match)
            else -> {
                loadingBar.visibility = View.VISIBLE
                registerUser(name, identifier, password)
            }
        }
    }

    private fun registerUser(name: String, identifier: String, password: String) {
        userRef.collection("Users").document(identifier).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    loadingBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        getString(R.string.user_already_exists, identifier),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    saveUserData(identifier, name, password)
                }
            }
    }

    private fun saveUserData(userID: String, name: String, password: String) {
        Calendar.getInstance()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HHmmss", Locale.getDefault())
        saveCurrentDate = dateFormatter.format(LocalDateTime.now())
        saveCurrentTime = timeFormatter.format(LocalDateTime.now())
        val uniqueID = "$saveCurrentDate$saveCurrentTime${(100_000..999_999).random()}"
        hashedPassword = hashPassword(password)

        val userMap = mapOf(
            "UserID" to uniqueID,
            "UserName" to name,
            "UserIdentifier" to userID,
            "UserPassword" to hashedPassword,
            "UserStatus" to "active",
            "CategoryDeleted" to "false"
        )

        userRef.collection("Users").document(userID).set(userMap)
            .addOnSuccessListener {
                loadingBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.user_created_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(this)
                }
            }
            .addOnFailureListener {
                loadingBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.user_not_created), Toast.LENGTH_SHORT)
                    .show()
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
