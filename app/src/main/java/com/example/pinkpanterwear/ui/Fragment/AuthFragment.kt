package com.example.pinkpanterwear.ui.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.MainActivity
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.ui.activities.ForgotPasswordActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: View
    private lateinit var forgotText: View

    private val authHelper = AuthHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        emailEditText = view.findViewById(R.id.email)
        passwordEditText = view.findViewById(R.id.password)
        loginButton = view.findViewById(R.id.btnLogin)
        registerText = view.findViewById(R.id.txtRegister)
        forgotText = view.findViewById(R.id.txtForgot)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Introduce email y contraseña", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            animateButton(loginButton)
            loginUser(email, password)
        }

        registerText.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Función de registro aún no implementada",
                Toast.LENGTH_SHORT
            ).show()
        }

        forgotText.setOnClickListener {
            startActivity(Intent(requireContext(), ForgotPasswordActivity::class.java))
        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        lifecycleScope.launch {
            val result = authHelper.loginUser(email, password)

            result.onSuccess {
                Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT)
                    .show()
                (activity as? MainActivity)?.navigateToHome()
            }

            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Error de autenticación: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun animateButton(button: View) {
        val anim = AlphaAnimation(0.6f, 1f)
        anim.duration = 200
        button.startAnimation(anim)
    }
}