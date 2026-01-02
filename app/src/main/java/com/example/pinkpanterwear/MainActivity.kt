package com.example.pinkpanterwear

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pinkpanterwear.databinding.ActivityMainBinding
import com.example.pinkpanterwear.ui.Fragment.HomeFragment
import com.example.pinkpanterwear.ui.Fragment.AuthFragment
import com.example.pinkpanterwear.ui.Fragment.UserCategoryFragment
import com.example.pinkpanterwear.ui.Fragment.UserCartFragment
import com.example.pinkpanterwear.ui.Fragment.UserHelpFragment
import com.example.pinkpanterwear.ui.Fragment.UserAccountFragment
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.ui.Fragment.StorePlaceholderFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authHelper = AuthHelper()

        if (savedInstanceState == null) {
            if (authHelper.isAuthenticated()) {
                navigateToHome()
            } else {
                replaceFragment(AuthFragment(), false)
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (!authHelper.isAuthenticated() && item.itemId != R.id.bottom_nav_home) {
                replaceFragment(AuthFragment(), false)
                return@setOnItemSelectedListener false
            }

            when (item.itemId) {
                R.id.bottom_nav_home -> navigateToHome()
                R.id.bottom_nav_category -> replaceFragment(UserCategoryFragment())
                R.id.bottom_nav_cart -> replaceFragment(UserCartFragment())
                R.id.bottom_nav_help -> replaceFragment(UserHelpFragment())
                R.id.bottom_nav_account -> replaceFragment(UserAccountFragment())
                R.id.bottom_nav_store -> replaceFragment(StorePlaceholderFragment())
                else -> return@setOnItemSelectedListener false
            }
            true
        }
    }

    fun navigateToHome() {
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment, showBottomNav: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        binding.bottomNavigationView.visibility = if (showBottomNav) View.VISIBLE else View.GONE
    }
}