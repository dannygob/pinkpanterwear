package com.example.pinkpanterwear.ui.activities

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pinkpanterwear.ui.Fragment.AdminAllOrdersFragment
import com.google.android.material.navigation.NavigationView

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.admin_fragment_container, AdminAllOrdersFragment())
                .commitNow() // Use commitNow for initial fragment if possible, or ensure navView exists
            navView.setCheckedItem(R.id.nav_admin_all_orders)
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // Ensure this drawable exists
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_admin_all_orders -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.admin_fragment_container, AdminAllOrdersFragment())
                        .commit()
                }

                R.id.nav_admin_products -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.admin_fragment_container, AdminProductsFragment())
                        .commit()
                }
                // Add other cases here
            }
            true
        }

        // Set checked item after listener is set and fragment loaded, if first launch
        if (savedInstanceState == null) {
            navView.setCheckedItem(R.id.nav_admin_all_orders);
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}