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
import com.example.pink.activities.LoginActivity
import com.example.pink.activities.RegisterActivity
import com.example.pink.activities.adminHomeActivity
import com.google.android.material.textview.MaterialTextView

class UserAccountFragment : Fragment() {
    private var user_account_admin: MaterialTextView? = null
    private var register_link_btn: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_user_account, container, false)

        user_account_admin = view.findViewById<View?>(R.id.user_account_admin) as MaterialTextView

        register_link_btn = view.findViewById<View?>(R.id.user_register_link_btn) as Button
        register_link_btn!!.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val intent = Intent(context, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        )


        val verify_link_btn = view.findViewById<View?>(R.id.account_my_wishlist) as TextView
        verify_link_btn.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        )



        user_account_admin!!.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val intent = Intent(context, adminHomeActivity::class.java)
                    startActivity(intent)
                }
            }
        )


        return view
    }
}