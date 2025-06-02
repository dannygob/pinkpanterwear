package com.example.pinkpanterwear.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import com.example.pinkpanterwear.AdminHomeActivity
import com.example.pinkpanterwear.AuthHelper
import com.google.android.material.textview.MaterialTextView
import com.example.pinkpanterwear.R // Make sure this R is correct for your project

class UserAccountFragment : Fragment() {

    private lateinit var userAccountNameTextView: MaterialTextView
    private lateinit var userRegisterLinkButton: Button
    private lateinit var myWishlistTextView: MaterialTextView
    private lateinit var myOrdersTextView: MaterialTextView
    private lateinit var recentSearchesTextView: MaterialTextView
    private lateinit var recentViewsTextView: MaterialTextView
    private lateinit var shippingInfoTextView: MaterialTextView
    private lateinit var adminTextView: MaterialTextView
    private lateinit var settingsTextView: MaterialTextView
    private lateinit var logoutTextView: MaterialTextView
    private lateinit var authHelper: AuthHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_account, container, false)

        // Basic log statement
        Log.d("UserAccountFragment", "onCreateView")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authHelper = AuthHelper()

        // Get references to views (Moved from onCreateView)
        userAccountNameTextView = view.findViewById(R.id.user_account_name)
        userRegisterLinkButton = view.findViewById(R.id.user_register_link_btn)
        myWishlistTextView = view.findViewById(R.id.account_my_wishlist)
        myOrdersTextView = view.findViewById(R.id.account_my_orders)
        recentSearchesTextView = view.findViewById(R.id.account_my_orders0) // Note: ID seems a bit off
        recentViewsTextView = view.findViewById(R.id.account_my_orders00) // Note: ID seems a bit off
        shippingInfoTextView = view.findViewById(R.id.account_myorders) // Note: ID seems a bit off
        adminTextView = view.findViewById(R.id.user_account_admin)
        adminTextView.visibility = View.GONE // Initially hide

        if (authHelper.isCurrentUserAdmin()) {
            adminTextView.visibility = View.VISIBLE
            adminTextView.setOnClickListener {
                // Log.d("UserAccountFragment", "Admin clicked - navigating") // Optional: keep or remove log
                val intent = Intent(activity, AdminHomeActivity::class.java)
                startActivity(intent)
            }
        } else {
            // Ensure listener is not active or does nothing if user is not admin and somehow visible
            adminTextView.setOnClickListener(null)
        }
        settingsTextView = view.findViewById(R.id.user_account_setting)
        logoutTextView = view.findViewById(R.id.logout)

        // TODO: Load user data and update UI elements (e.g., userAccountNameTextView, userImageView)
        // Example: userAccountNameTextView.text = "Current User Name"

        // Set up click listeners for account options
        userRegisterLinkButton.setOnClickListener {
            Log.d("UserAccountFragment", "Register/Login button clicked")
        }

        myWishlistTextView.setOnClickListener {
            Log.d("UserAccountFragment", "My Wishlist clicked")
        }
        myOrdersTextView.setOnClickListener {
            Log.d("UserAccountFragment", "My Orders clicked")
        }
        recentSearchesTextView.setOnClickListener {
            Log.d("UserAccountFragment", "Recent searches clicked")
        }
        recentViewsTextView.setOnClickListener {
            Log.d("UserAccountFragment", "Recent views clicked")
        }
        shippingInfoTextView.setOnClickListener {
            Log.d("UserAccountFragment", "Shipping Information clicked")
        }
        // adminTextView.setOnClickListener has been handled above based on auth status
        settingsTextView.setOnClickListener {
            Log.d("UserAccountFragment", "Settings clicked")
        }
        logoutTextView.setOnClickListener {
            Log.d("UserAccountFragment", "Logout clicked")
            // Implement logout logic
        }
    }
}