package com.example.pink.Activities

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
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.MainActivity
import com.example.pink.Model.Categories
import com.example.pink.R
import com.example.pink.ViewHolder.CategoryViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class adminCategoriesActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var addCategoryButton: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar2: ProgressBar

    private val categoriesRef = FirebaseFirestore.getInstance()
    private val query: Query = categoriesRef.collection("Categories")
    private var adapter: FirestorePagingAdapter<Categories, CategoryViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        addCategoryButton = findViewById(R.id.admin_go_to_add_category_page_btn)
        progressBar2 = findViewById(R.id.progressBar2)
        recyclerView = findViewById(R.id.recycler_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.openNavDrawer, R.string.closeNavDrawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        addCategoryButton.setOnClickListener {
            startActivity(Intent(this, adminCategoriesAddEditActivity::class.java))
        }

        recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(this@adminCategoriesActivity, 1)
        }

        setupAdapter()
    }

    private fun setupAdapter() {
        val config = PagingConfig(pageSize = 20, enablePlaceholders = false)

        val options = FirestorePagingOptions.Builder<Categories>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Categories::class.java)
            .build()

        adapter = object : FirestorePagingAdapter<Categories, CategoryViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.admin_category_display_layout, parent, false)
                return CategoryViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: CategoryViewHolder,
                position: Int,
                model: Categories,
            ) {
                holder.txtCategoryName?.text = model.categoryName
                holder.txtCategoryStatus?.text = model.categoryStatus
                Picasso.get().load(model.categoryImage).into(holder.txtCategoryImage)

                holder.itemView.setOnClickListener {
                    val intent = Intent(
                        this@adminCategoriesActivity,
                        AdminProductsDetailsActivity::class.java
                    ).apply {
                        putExtra("ProductUniqueID", model.categoryUniqueID)
                    }
                    startActivity(intent)
                }
            }
        }

        recyclerView.adapter = adapter
        progressBar2.visibility = View.VISIBLE

        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                progressBar2.visibility = View.GONE
            }
        })
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
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
}
