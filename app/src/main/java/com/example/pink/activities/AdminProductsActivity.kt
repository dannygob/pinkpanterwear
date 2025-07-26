package com.example.pink.activities

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.MainActivity
import com.example.pink.R
import com.example.pink.model.Products
import com.example.pink.viewHolder.ProductViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class AdminProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var addProductButton: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val query: Query = FirebaseFirestore.getInstance().collection("Products")
    private var adapter: FirestorePagingAdapter<Products, ProductViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        progressBar = findViewById(R.id.progressBar2)
        addProductButton = findViewById(R.id.admin_go_to_add_product_page_btn)
        recyclerView = findViewById(R.id.recycler_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.openNavDrawer, R.string.closeNavDrawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        addProductButton.setOnClickListener {
            startActivity(Intent(this, AdminProductsAddEditActivity::class.java))
        }

        recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@AdminProductsActivity)
        }

        setupAdapter()
    }

    private fun setupAdapter() {
        val config = PagingConfig(pageSize = 10, enablePlaceholders = false)

        val options = FirestorePagingOptions.Builder<Products>()
            .setLifecycleOwner(this)
            .setQuery(query, config, Products::class.java)
            .build()

        adapter = object : FirestorePagingAdapter<Products, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.admin_products_display_layout, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: ProductViewHolder,
                position: Int,
                model: Products,
            ) {
                holder.txtProductName?.text = model.productName
                holder.txtProductPrice?.text =
                    "${getString(R.string.currency)} ${model.productPrice}"
                holder.txtProductDescription?.text = model.productDescription

                Picasso.get()
                    .load(model.productImage)
                    .placeholder(R.drawable.ic_baseline_insert_photo_24)
                    .error(R.drawable.ic_error)
                    .into(holder.txtProductImage)

                holder.itemView.setOnClickListener {
                    Intent(
                        this@AdminProductsActivity,
                        AdminProductsDetailsActivity::class.java
                    ).apply {
                        putExtra("ProductUniqueID", model.productUniqueID)
                        startActivity(this)
                    }
                }
            }
        }

        recyclerView.adapter = adapter
        progressBar.visibility = View.VISIBLE

        adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                progressBar.visibility = View.GONE
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
        val destination = when (item.itemId) {
            R.id.admin_home_menu -> AdminHomeActivity::class.java
            R.id.admin_orders_menu -> AdminOrdersActivity::class.java
            R.id.admin_categories_menu -> AdminCategoriesActivity::class.java
            R.id.admin_delivery_menu -> AdminDeliveryActivity::class.java
            R.id.admin_assistant_menu -> AdminAssistantActivity::class.java
            R.id.admin_main_home_menu -> MainActivity::class.java
            R.id.admin_logout_menu -> {
                Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_SHORT).show()
                null
            }
            // Ya estás en AdminProductsActivity
            R.id.admin_products_menu -> null
            else -> null
        }

        destination?.let { startActivity(Intent(this, it)) }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}