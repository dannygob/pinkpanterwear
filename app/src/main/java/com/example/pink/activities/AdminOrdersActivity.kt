package com.example.pink.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.pink.MainActivity
import com.example.pink.R
import com.example.pink.adapter.AdminOrdersPagerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AdminOrdersActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_orders)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.openNavDrawer, R.string.closeNavDrawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        viewPager2 = findViewById(R.id.view_pager)
        viewPager2.adapter = AdminOrdersPagerAdapter(this)

        tabLayout = findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.orCreateBadge.apply {
                backgroundColor = ContextCompat.getColor(
                    this@AdminOrdersActivity,
                    R.color.design_default_color_error
                )
                isVisible = true
                number = 50
                maxCharacterCount = 10
            }

            when (position) {
                0 -> {
                    tab.text = getString(R.string.new_orders)
                    tab.setIcon(R.drawable.ic_baseline_person_24)
                }
                1 -> {
                    tab.text = getString(R.string.dispatch_orders)
                    tab.setIcon(R.drawable.ic_baseline_directions_bike_24)
                }
                2 -> {
                    tab.text = getString(R.string.all_orders)
                    tab.setIcon(R.drawable.ic_baseline_local_grocery_store_24)
                }
            }
        }.attach()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destination = when (item.itemId) {
            R.id.admin_home_menu -> AdminHomeActivity::class.java
            R.id.admin_categories_menu -> AdminCategoriesActivity::class.java
            R.id.admin_products_menu -> AdminProductsActivity::class.java
            R.id.admin_delivery_menu -> AdminDeliveryActivity::class.java
            R.id.admin_assistant_menu -> AdminAssistantActivity::class.java
            R.id.admin_main_home_menu -> MainActivity::class.java
            R.id.admin_logout_menu -> {
                Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_SHORT).show()
                null
            }
            // Ya estás en AdminOrdersActivity
            R.id.admin_orders_menu -> null
            else -> null
        }

        destination?.let {
            startActivity(Intent(this, it))
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}