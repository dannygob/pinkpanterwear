package com.example.pinkpanterwear // Replace with your actual package name

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pinkpanterwear.databinding.ActivityMainBinding
import com.example.pinkpanterwear.ui.account.UserAccountFragment // Adjust import based on your fragment's package
import com.example.pinkpanterwear.ui.cart.UserCartFragment // Adjust import based on your fragment's package
import com.example.pinkpanterwear.ui.category.UserCategoryFragment // Adjust import based on your fragment's package
import com.example.pinkpanterwear.ui.help.UserHelpFragment // Adjust import based on your fragment's package
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.ui.AuthFragment
import com.example.pinkpanterwear.ui.home.HomeFragment // Adjust import based on your fragment's package
import com.example.pinkpanterwear.ui.store.StorePlaceholderFragment // Create this fragment or replace with actual

class MainActivity : AppCompatActivity() {

    fun navigateToHome() {
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authHelper = AuthHelper()

        if (savedInstanceState == null) {
            if (authHelper.isAuthenticated()) {
                replaceFragment(HomeFragment())
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                replaceFragment(AuthFragment())
                binding.bottomNavigationView.visibility = View.GONE
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            // If user gets here and is not authenticated (e.g. back button), and tries to navigate, send to AuthFragment
            // Allow Home selection to proceed to default auth check logic
            if (!authHelper.isAuthenticated() && item.itemId != R.id.bottom_nav_home) {
                replaceFragment(AuthFragment())
                binding.bottomNavigationView.visibility = View.GONE;
                return@setOnItemSelectedListener false; // Or true if you want to mark it as handled but still go to auth
            }
            when (item.itemId) {
                R.id.bottom_nav_home -> {
                    // Handled by the auth check logic or navigateToHome
                    if (authHelper.isAuthenticated()) {
                        replaceFragment(HomeFragment())
                    } else {
                        replaceFragment(AuthFragment())
                        binding.bottomNavigationView.visibility = View.GONE
                    }
                    true
                }
                R.id.bottom_nav_category -> {
                    replaceFragment(UserCategoryFragment())
                    true
                }
                R.id.bottom_nav_cart -> {
                    replaceFragment(UserCartFragment())
                    true
                }
                R.id.bottom_nav_help -> {
                    replaceFragment(UserHelpFragment())
                    true
                }
                R.id.bottom_nav_account -> {
                    replaceFragment(UserAccountFragment())
                    true
                }
                R.id.bottom_nav_store -> {
                    // Placeholder for the store fragment
                    replaceFragment(StorePlaceholderFragment()) // Replace with your actual Store Fragment
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

// You'll need to create this placeholder fragment or replace it with your actual Store Fragment
class StorePlaceholderFragment : Fragment() {
    // Implement the fragment's layout and logic here
}