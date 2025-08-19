package com.example.pink.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pink.R
import com.example.pink.fragment.UserCategoryProductsFragment

class UserCategoryProductsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_category_products) // Assuming you have this layout

        if (savedInstanceState == null) {
            val fragment = UserCategoryProductsFragment()
            fragment.arguments = intent.extras // Pass arguments from intent to fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Assuming a container ID
                .commit()
        }
    }
}
