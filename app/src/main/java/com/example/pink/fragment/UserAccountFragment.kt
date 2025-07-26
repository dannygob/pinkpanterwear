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
    private var userAccountAdmin: MaterialTextView? = null
    private var registerLinkBtn: Button? = null
    private var loginLinkBtn: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_user_account, container, false)

        userAccountAdmin = view.findViewById(R.id.user_account_admin)
        registerLinkBtn = view.findViewById(R.id.user_register_link_btn)
        loginLinkBtn = view.findViewById(R.id.account_my_wishlist)

        registerLinkBtn?.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        }

        loginLinkBtn?.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        userAccountAdmin?.setOnClickListener {
            startActivity(Intent(context, AdminHomeActivity::class.java))
        }

        return view
    }
}
}
