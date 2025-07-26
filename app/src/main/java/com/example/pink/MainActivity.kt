package com.example.pink

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pink.fragment.UserAccountFragment
import com.example.pink.fragment.UserCartFragment
import com.example.pink.fragment.UserCategoryFragment
import com.example.pink.fragment.UserHelpFragment
import com.example.pink.fragment.UserHomeFragment
import com.example.pink.prevalent.Prevalent
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupBottomNavigation()
        loadInitialFragment()
        loadCartData()
        updateCartBadge()
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val selectedFragment = when (item.itemId) {
                R.id.bottom_nav_home -> UserHomeFragment()
                R.id.bottom_nav_category -> UserCategoryFragment()
                R.id.bottom_nav_cart -> UserCartFragment()
                R.id.bottom_nav_help -> UserHelpFragment()
                R.id.bottom_nav_account -> UserAccountFragment()
                else -> UserHomeFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }
    }

    private fun loadInitialFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UserHomeFragment())
            .commit()
    }

    private fun loadCartData() {
        Prevalent.getCartItems(this)
    }

    private fun updateCartBadge() {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart)
        badge.isVisible = true
        badge.number = Prevalent.getCartItemsCount()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.exit_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                finishAffinity()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Prevalent.saveCartItems(this)
    }
}