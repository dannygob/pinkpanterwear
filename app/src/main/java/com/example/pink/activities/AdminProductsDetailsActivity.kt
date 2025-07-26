package com.example.pink.activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pink.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AdminProductsDetailsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var productImage: ShapeableImageView
    private lateinit var deleteProductButton: Button
    private lateinit var editProductButton: Button
    private lateinit var productName: MaterialTextView
    private lateinit var productPrice: MaterialTextView
    private lateinit var productQuantity: MaterialTextView
    private lateinit var productDescription: MaterialTextView

    private val productRef = FirebaseFirestore.getInstance()
    private var productID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_details)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        productID = intent.getStringExtra("ProductUniqueID")

        productName = findViewById(R.id.product_details_name)
        productPrice = findViewById(R.id.product_details_price)
        productQuantity = findViewById(R.id.product_details_quantity)
        productDescription = findViewById(R.id.product_details_description)
        deleteProductButton = findViewById(R.id.delete_product)
        editProductButton = findViewById(R.id.edit_product)
        productImage = findViewById(R.id.product_details_image)

        productID?.let {
            loadProductDetails(it)
        } ?: Toast.makeText(this, getString(R.string.product_not_specified), Toast.LENGTH_SHORT)
            .show()
    }

    private fun loadProductDetails(productID: String) {
        productRef.collection("Products").document(productID)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    productName.text = document.getString("ProductName")
                    productPrice.text =
                        "${getString(R.string.currency)} ${document.getString("ProductPrice")}"
                    productDescription.text = document.getString("ProductDescription")
                    productQuantity.text = document.getString("ProductStatus")
                    Picasso.get()
                        .load(document.getString("ProductImage"))
                        .placeholder(R.drawable.ic_baseline_insert_photo_24)
                        .error(R.drawable.ic_error)
                        .into(productImage)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.product_does_not_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.could_not_load_product), Toast.LENGTH_SHORT)
                    .show()
            }
    }
}