package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.toString

class AuthFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    private val authHelper = AuthHelper() // Instantiate AuthHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        // Get references to UI elements
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        registerButton = view.findViewById(R.id.register_button)
        loginButton = view.findViewById(R.id.login_button)

        // Add click listeners for buttons
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the registration function
                registerUser(email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the login function
                loginUser(email, password)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val forgotPasswordLink: TextView = view.findViewById(R.id.forgot_password_link)
        forgotPasswordLink.setOnClickListener {
            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun registerUser(email: String, password: String) {
        lifecycleScope.launch {
            val result = authHelper.registerUser(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT)
                    .show()
                (activity as? MainActivity)?.navigateToHome()
            } else {
                val exception = result.exceptionOrNull()
                Toast.makeText(
                    requireContext(),
                    "Registration failed: ${exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO: Show more specific error messages based on exception type
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            val result = authHelper.loginUser(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                (activity as? MainActivity)?.navigateToHome()
            } else {
                val exception = result.exceptionOrNull()
                Toast.makeText(
                    requireContext(),
                    "Login failed: ${exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO: Show more specific error messages based on exception type
            }
        }
    }

    // You might want to add a logout function here as well, similar to the AuthHelper
    // private fun logoutUser() {
    //     authHelper.logoutUser()
    //     Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
    //     // TODO: Navigate back to login/registration screen or update UI
    // }
}