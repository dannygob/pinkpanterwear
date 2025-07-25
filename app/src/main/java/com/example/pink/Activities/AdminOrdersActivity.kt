package com.example.pink.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.pink.Adapter.AdminOrdersPagerAdapter
import com.example.pink.MainActivity
import com.example.pink.R
import com.google.android.material.badge.BadgeDrawable
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
            this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        viewPager2 = findViewById(R.id.view_pager)
        viewPager2.adapter = AdminOrdersPagerAdapter(this)

        tabLayout = findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            val badgeDrawable: BadgeDrawable = tab.orCreateBadge
            badgeDrawable.backgroundColor =
                ContextCompat.getColor(applicationContext, R.color.design_default_color_error)
            badgeDrawable.isVisible = true
            badgeDrawable.number = 50
            badgeDrawable.maxCharacterCount = 10

            when (position) {
                0 -> {
                    tab.text = "New"
                    tab.setIcon(R.drawable.ic_baseline_person_24)
                }

                1 -> {
                    tab.text = "Dispatch"
                    tab.setIcon(R.drawable.ic_baseline_directions_bike_24)
                }

                2 -> {
                    tab.text = "All"
                    tab.setIcon(R.drawable.ic_baseline_local_grocery_store_24)
                }
            }
        }.attach()
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
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        // Si necesitas detectar cambios del puntero, implementa aquí
    }
}
