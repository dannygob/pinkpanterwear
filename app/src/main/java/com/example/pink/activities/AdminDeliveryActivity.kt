package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pink.MainActivity
import com.example.pink.R
import com.google.android.material.navigation.NavigationView

class AdminDeliveryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delivery)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destination = when (item.itemId) {
            R.id.admin_home_menu -> AdminHomeActivity::class.java
            R.id.admin_orders_menu -> AdminOrdersActivity::class.java
            R.id.admin_categories_menu -> AdminCategoriesActivity::class.java
            R.id.admin_products_menu -> AdminProductsActivity::class.java
            R.id.admin_delivery_menu -> AdminDeliveryActivity::class.java
            R.id.admin_assistant_menu -> AdminAssistantActivity::class.java
            R.id.admin_main_home_menu -> MainActivity::class.java
            R.id.admin_settings_menu -> AdminAssistantActivity::class.java
            R.id.admin_logout_menu -> {
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                null
            }

            else -> null
        }

        destination?.let {
            startActivity(Intent(this, it))
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}