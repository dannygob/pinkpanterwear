package com.example.pink

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pink.Fragment.UserHomeFragment
import com.example.pink.Fragment.UserCategoryFragment
import com.example.pink.Fragment.UserCartFragment
import com.example.pink.Fragment.UserHelpFragment
import com.example.pink.Fragment.UserAccountFragment
import com.example.pink.Prevalent.Prevalent
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener(navListener)

        // Fragmento inicial
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UserHomeFragment())
            .commit()

        // Load CartItems from Shared Preferences
        Prevalent.CartItems = Prevalent.getCartItems(this)

        // Badge del carrito
        bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart).apply {
            isVisible = true
            number = Prevalent.CartItems
        }
    }

    // Listener de navegación
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ ->
                    finishAffinity()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Save CartItems to Shared Preferences
        Prevalent.saveCartItems(this, Prevalent.CartItems)
    }
}
