package com.example.slickkwear

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.slickkwear.Model.Categories
import com.example.slickkwear.ViewHolder.CategoryViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class AdminCategoriesActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    //Choose android x version
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var addCategoryButton: MaterialButton? = null

    private var categoriesRef: FirebaseFirestore? = null
    private var query: Query? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private val progressBar1: ProgressBar? = null
    private var progressBar2: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories)

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

        progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)

        addCategoryButton = findViewById<MaterialButton>(R.id.admin_go_to_add_category_page_btn)

        addCategoryButton.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(
                    applicationContext,
                    AdminCategoriesAddEditActivity::class.java
                )
                startActivity(intent)
            }
        )


        categoriesRef = FirebaseFirestore.getInstance()
        query = categoriesRef!!.collection("Categories")

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(this);
        layoutManager = GridLayoutManager(this, 1)
        recyclerView.setLayoutManager(layoutManager)
    }

    override fun onStart() {
        super.onStart()

        val options = FirestoreRecyclerOptions.Builder<Categories>()
            .setQuery(query!!, Categories::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Categories, CategoryViewHolder> =
            object : FirestoreRecyclerAdapter<Categories, CategoryViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): CategoryViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.admin_category_display_layout, parent, false)
                    return CategoryViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: CategoryViewHolder,
                    position: Int,
                    model: Categories
                ) {
                    holder.txtCategoryName.text = model.categoryName
                    Picasso.get().load(model.categoryImage).into(holder.txtCategoryImage)
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            applicationContext,
                            AdminCategoriesAddEditActivity::class.java
                        )
                        intent.putExtra("CategoryUniqueID", model.categoryUniqueID)
                        startActivity(intent)
                    }
                }
            }

        recyclerView!!.adapter = adapter
        adapter.startListening()
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