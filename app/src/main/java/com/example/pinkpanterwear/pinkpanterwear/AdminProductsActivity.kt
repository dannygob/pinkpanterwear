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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.slickkwear.Model.Products
import com.example.slickkwear.ViewHolder.ProductViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class AdminProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //Choose android x version
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var navigationView: NavigationView? = null
    private var addProductButton: MaterialButton? = null

    private var productsRef: FirebaseFirestore? = null
    private var query: Query? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private val progressBar1: ProgressBar? = null
    private var progressBar2: ProgressBar? = null

    //    private SwipeRefreshLayout mSwipeRefreshLayout;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products)

        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        //        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

        //        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)

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


        addProductButton = findViewById<MaterialButton>(R.id.admin_go_to_add_product_page_btn)

        addProductButton.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(
                    applicationContext,
                    AdminProductsAddEditActivity::class.java
                )
                startActivity(intent)
            }
        )



        productsRef = FirebaseFirestore.getInstance()
        query = productsRef!!.collection("Products")

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
        layoutManager = LinearLayoutManager(this)
        //        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager)
    }

    override fun onStart() {
        super.onStart()

        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(query!!, Products::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Products, ProductViewHolder> =
            object : FirestoreRecyclerAdapter<Products, ProductViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): ProductViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.admin_products_display_layout, parent, false)
                    return ProductViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: ProductViewHolder,
                    position: Int,
                    model: Products
                ) {
                    holder.txtProductName.text = model.productName
                    holder.txtProductPrice.text = "Ksh " + model.productPrice
                    holder.txtProductDescription.text = model.productDescription
                    Picasso.get().load(model.productImage).into(holder.txtProductImage)
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            applicationContext,
                            AdminProductsDetailsActivity::class.java
                        )
                        intent.putExtra("ProductUniqueID", model.productUniqueID)
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