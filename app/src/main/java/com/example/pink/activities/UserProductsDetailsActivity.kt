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
        toolbar.navigationIcon = getDrawable(R.drawable.ic_baseline_arrow_back_ios_24)

        productID = intent.getStringExtra("ProductUniqueID")

        productImage = findViewById(R.id.user_product_details_image)
        userProductDetailsName = findViewById(R.id.user_product_details_name)
        userProductDetailsPrice = findViewById(R.id.user_product_details_price)
        userProductDetailsDescription = findViewById(R.id.user_product_details_description)
        userProductDetailsSize = findViewById(R.id.user_product_details_size)

        productSize()

        productRef = FirebaseFirestore.getInstance()

        getProductDetails(productID)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductDetails(productID: String?) {
        if (productID.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.product_not_specified), Toast.LENGTH_SHORT)
                .show()
            return
        }

        val productItem = productRef.collection("Products").document(productID)
        productItem.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Picasso.get().load(documentSnapshot.getString("ProductImage"))
                        .into(productImage)
                    userProductDetailsName.text = documentSnapshot.getString("ProductName")
                    userProductDetailsPrice.text =
                        "${getString(R.string.currency)} ${documentSnapshot.getString("ProductPrice")}"
                    userProductDetailsDescription.text =
                        documentSnapshot.getString("ProductDescription")
                } else {
                    Toast.makeText(
                        this@UserProductsDetailsActivity,
                        getString(R.string.product_does_not_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun productSize() {
        for (i in 36..42) {
            val valueTV = TextView(applicationContext)
            valueTV.text = Html.fromHtml(
                "<strike><font color='#757575'>$i</font></strike>",
                Html.FROM_HTML_MODE_LEGACY
            )
            valueTV.id = "102$i".toInt()
            valueTV.setPadding(15, 15, 15, 15)
            valueTV.setBackgroundResource(R.drawable.user_product_details_size_bg)
            valueTV.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(5, 5, 5, 5)
            valueTV.layoutParams = params

            userProductDetailsSize.addView(valueTV)
        }
    }
}
