package com.example.slickkwear

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pinkpanterwear.R
import com.google.android.material.navigation.NavigationView

class AdminSettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //Choose android x version
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_settings)

        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        //        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

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
        if (item.itemId == R.id.admin_home_menu) {
            val intent = Intent(
                applicationContext,
                AdminHomeActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_orders_menu) {
            val intent = Intent(
                applicationContext,
                AdminOrdersActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_categories_menu) {
            val intent = Intent(
                applicationContext,
                AdminCategoriesActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_products_menu) {
            val intent = Intent(
                applicationContext,
                AdminProductsActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_delivery_menu) {
            val intent = Intent(
                applicationContext,
                AdminDeliveryActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_assistant_menu) {
            val intent = Intent(
                applicationContext,
                AdminAssistantActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_main_home_menu) {
            val intent = Intent(
                applicationContext,
                MainActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_settings_menu) {
            val intent = Intent(
                applicationContext,
                AdminAssistantActivity::class.java
            )
            startActivity(intent)
        } else if (item.itemId == R.id.admin_logout_menu) {
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
    }
}