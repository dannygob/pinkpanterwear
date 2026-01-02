package com.example.pinkpanterwear.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pinkpanterwear.MainActivity
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.ui.Fragment.AdminAllOrdersFragment
import com.example.pinkpanterwear.ui.Fragment.AdminCancelledOrdersFragment
import com.example.pinkpanterwear.ui.Fragment.AdminDeliveredOrdersFragment
import com.example.pinkpanterwear.ui.Fragment.AdminProductsFragment
import com.example.pinkpanterwear.ui.Fragment.AdminReturnedOrdersFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

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
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Load initial fragment only once
        if (savedInstanceState == null) {
            loadFragment(AdminAllOrdersFragment())
            navView.setCheckedItem(R.id.nav_admin_all_orders)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {

                R.id.nav_admin_all_orders -> {
                    loadFragment(AdminAllOrdersFragment())
                }

                R.id.nav_admin_products -> {
                    loadFragment(AdminProductsFragment())
                }

                R.id.nav_admin_cancelled_orders -> {
                    loadFragment(AdminCancelledOrdersFragment())
                }

                R.id.nav_admin_delivered_orders -> {
                    loadFragment(AdminDeliveredOrdersFragment())
                }

                R.id.nav_admin_returned_orders -> {
                    loadFragment(AdminReturnedOrdersFragment())
                }

                // ⭐ LOGOUT CORRECTO PARA TU ARQUITECTURA
                R.id.nav_admin_logout -> {
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish()
                }
            }

            menuItem.isChecked = true
            true
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.admin_fragment_container, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}