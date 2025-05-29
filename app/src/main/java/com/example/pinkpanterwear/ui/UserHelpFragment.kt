package com.example.pinkpanterwear.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pinkpanterwear.R

class UserHelpFragment : Fragment() {

    private val TAG = "UserHelpFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_help, container, false)

        // Get references to the help views
 val helpTitle = view.findViewById<TextView>(R.id.help_title)
 val helpContent = view.findViewById<TextView>(R.id.help_content)

 // Get references to the FAQ views
 val faqTitle = view.findViewById<TextView>(R.id.faq_title)
 val faqQ1 = view.findViewById<TextView>(R.id.faq_q1)
 val faqA1 = view.findViewById<TextView>(R.id.faq_a1)
registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the registration function
                registerUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the login function
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

 // TODO: Load dynamic help content here if needed

 return view
    }
}