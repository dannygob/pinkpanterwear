package com.example.pink.activities

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pink.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class UserProductsDetailsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var productRef: FirebaseFirestore
    private lateinit var userProductDetailsName: MaterialTextView
    private lateinit var userProductDetailsPrice: MaterialTextView
    private lateinit var userProductDetailsDescription: MaterialTextView
    private lateinit var productImage: ShapeableImageView
    private lateinit var userProductDetailsSize: LinearLayout

    private var productID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products_details)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)

        productID = intent.getStringExtra("ProductUniqueID")

        productImage = findViewById(R.id.user_product_details_image)
        userProductDetailsName = findViewById(R.id.user_product_details_name)
        userProductDetailsPrice = findViewById(R.id.user_product_details_price)
        userProductDetailsDescription = findViewById(R.id.user_product_details_description)
        userProductDetailsSize = findViewById(R.id.user_product_details_size)

        generateSizeViews()

        productRef = FirebaseFirestore.getInstance()
        productID?.let { loadProductDetails(it) }
            ?: showToast(getString(R.string.product_not_specified))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun loadProductDetails(productID: String) {
        productRef.collection("Products").document(productID).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Picasso.get()
                        .load(document.getString("ProductImage"))
                        .placeholder(R.drawable.ic_baseline_insert_photo_24)
                        .error(R.drawable.ic_error)
                        .into(productImage)

                    userProductDetailsName.text = document.getString("ProductName")
                    userProductDetailsPrice.text =
                        "${getString(R.string.currency)} ${document.getString("ProductPrice")}"
                    userProductDetailsDescription.text = document.getString("ProductDescription")
                } else {
                    showToast(getString(R.string.product_does_not_exist))
                }
            }
            .addOnFailureListener {
                showToast(getString(R.string.could_not_load_product))
            }
    }

    private fun generateSizeViews() {
        (36..42).forEach { size ->
            val sizeView = TextView(this).apply {
                text = Html.fromHtml(
                    "<strike><font color='#757575'>$size</font></strike>",
                    Html.FROM_HTML_MODE_LEGACY
                )
                id = "102$size".toInt()
                setPadding(15, 15, 15, 15)
                setBackgroundResource(R.drawable.user_product_details_size_bg)
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(5, 5, 5, 5)
                }
            }
            userProductDetailsSize.addView(sizeView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}