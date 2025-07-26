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

class AdminSettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.admin_home_menu -> startActivity(Intent(this, AdminHomeActivity::class.java))
            R.id.admin_orders_menu -> startActivity(Intent(this, AdminOrdersActivity::class.java))
            R.id.admin_categories_menu -> startActivity(
                Intent(
                    this,
                    AdminCategoriesActivity::class.java
                )
            )

            R.id.admin_products_menu -> startActivity(
                Intent(
                    this,
                    AdminProductsActivity::class.java
                )
            )

            R.id.admin_delivery_menu -> startActivity(
                Intent(
                    this,
                    AdminDeliveryActivity::class.java
                )
            )

            R.id.admin_assistant_menu -> startActivity(
                Intent(
                    this,
                    AdminAssistantActivity::class.java
                )
            )

            R.id.admin_main_home_menu -> startActivity(Intent(this, MainActivity::class.java))
            R.id.admin_settings_menu -> {
                // Do nothing, already in this activity
            }

            R.id.admin_logout_menu -> {
                Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
