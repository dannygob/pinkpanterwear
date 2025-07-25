package com.example.pink.Activities

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

class adminHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //Choose android x version
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        toolbar = findViewById<Toolbar?>(R.id.main_toolbar)
        //        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

        val actionBarDrawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )
        drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView!!.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.admin_home_menu -> startActivity(Intent(this, adminHomeActivity::class.java))
            R.id.admin_orders_menu -> startActivity(Intent(this, AdminOrdersActivity::class.java))
            R.id.admin_categories_menu -> startActivity(
                Intent(
                    this,
                    adminCategoriesActivity::class.java
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
                    adminDeliveryActivity::class.java
                )
            )

            R.id.admin_assistant_menu -> startActivity(
                Intent(
                    this,
                    adminAssistantActivity::class.java
                )
            )

            R.id.admin_main_home_menu -> startActivity(Intent(this, MainActivity::class.java))
            R.id.admin_logout_menu -> {
                // Implement logout logic here
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
    }
}
