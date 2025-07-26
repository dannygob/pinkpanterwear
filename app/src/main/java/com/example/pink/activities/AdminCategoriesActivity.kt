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
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.MainActivity
import com.example.pink.R
import com.example.pink.model.Categories
import com.example.pink.viewHolder.CategoryViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class AdminCategoriesActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var addCategoryButton: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        progressBar = findViewById(R.id.progressBar2)
        addCategoryButton = findViewById(R.id.admin_go_to_add_category_page_btn)
        recyclerView = findViewById(R.id.recycler_view)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.openNavDrawer,
            R.string.closeNavDrawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        addCategoryButton.setOnClickListener {
            startActivity(Intent(this, AdminCategoriesAddEditActivity::class.java))
        }

        query = FirebaseFirestore.getInstance()
            .collection("Categories")

        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = GridLayoutManager(this, 1)
    }

    override fun onStart() {
        super.onStart()

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(3)
            .setPageSize(2)
            .build()

        val options = FirestorePagingOptions.Builder<Categories>()
            .setQuery(query, config, Categories::class.java)
            .build()

        val adapter = object : FirestorePagingAdapter<Categories, CategoryViewHolder>(options) {
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
                holder.txtCategoryName.text = model.categoryName
                holder.txtCategoryStatus.text = model.categoryStatus
                Picasso.get().load(model.categoryImage).into(holder.txtCategoryImage)

                holder.itemView.setOnClickListener {
                    val intent = Intent(
                        this@AdminCategoriesActivity,
                        AdminProductsDetailsActivity::class.java
                    )
                    intent.putExtra("ProductUniqueID", model.categoryUniqueID)
                    startActivity(intent)
                }
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
                    LoadingState.LOADING_MORE, LoadingState.LOADING_INITIAL -> progressBar.visibility =
                        View.VISIBLE

                    LoadingState.LOADED, LoadingState.FINISHED -> progressBar.visibility = View.GONE
                    LoadingState.ERROR -> retry()
                }
            }
        }

        recyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val destination = when (item.itemId) {
            R.id.admin_home_menu -> AdminHomeActivity::class.java
            R.id.admin_orders_menu -> AdminOrdersActivity::class.java
            R.id.admin_categories_menu -> AdminCategoriesActivity::class.java
            R.id.admin_products_menu -> AdminProductsActivity::class.java
            R.id.admin_delivery_menu -> AdminDeliveryActivity::class.java
            R.id.admin_assistant_menu -> AdminAssistantActivity::class.java
            R.id.admin_main_home_menu -> MainActivity::class.java
            R.id.admin_settings_menu -> AdminAssistantActivity::class.java
            R.id.admin_logout_menu -> {
                Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                null
            }

            else -> null
        }

        destination?.let {
            startActivity(Intent(this, it))
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}