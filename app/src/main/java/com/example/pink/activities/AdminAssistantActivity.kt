package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.pink.MainActivity
import com.example.pink.R
import com.example.pink.databinding.ActivityAdminAssistantBinding
import com.google.android.material.navigation.NavigationView

class AdminAssistantActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAdminAssistantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAssistantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.mainToolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = when (item.itemId) {
            R.id.admin_home_menu -> Intent(this, AdminHomeActivity::class.java)
            R.id.admin_orders_menu -> Intent(this, AdminOrdersActivity::class.java)
            R.id.admin_categories_menu -> Intent(this, AdminCategoriesActivity::class.java)
            R.id.admin_products_menu -> Intent(this, AdminProductsActivity::class.java)
            R.id.admin_delivery_menu -> Intent(this, AdminDeliveryActivity::class.java)
            R.id.admin_assistant_menu -> Intent(this, AdminAssistantActivity::class.java)
            R.id.admin_main_home_menu -> Intent(this, MainActivity::class.java)
            R.id.admin_settings_menu -> Intent(this, AdminAssistantActivity::class.java)
            R.id.admin_logout_menu -> {
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                null
            }

            else -> null
        }

        intent?.let { startActivity(it) }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}