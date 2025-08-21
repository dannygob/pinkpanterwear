package com.example.pinkpanterwear.ui.activities


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pink.R
import com.example.pinkpanterwear.ui.Fragment.AdminAllOrdersFragment
import com.example.pinkpanterwear.ui.Fragment.AdminProductsFragment
import com.google.android.material.navigation.NavigationView

class AdminHomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // Ensure this drawable exists
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.admin_fragment_container, AdminAllOrdersFragment())
                .commitNow() // Use commitNow for initial fragment if possible, or ensure navView exists
        }

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
            // Set checked item after listener is set and fragment loaded, if first launch
            navView.setCheckedItem(menuItem.itemId)
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
