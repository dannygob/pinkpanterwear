package com.example.pink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pink.R
import com.example.pink.activities.AdminHomeActivity
import com.example.pink.activities.LoginActivity
import com.example.pink.activities.RegisterActivity
import com.google.android.material.textview.MaterialTextView

class UserAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)

        val adminLink: MaterialTextView = view.findViewById(R.id.user_account_admin)
        val registerBtn: Button = view.findViewById(R.id.user_register_link_btn)
        val loginLink: TextView = view.findViewById(R.id.account_my_wishlist)

        registerBtn.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }

        loginLink.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        adminLink.setOnClickListener {
            startActivity(Intent(requireContext(), AdminHomeActivity::class.java))
        }

        return view
    }
}