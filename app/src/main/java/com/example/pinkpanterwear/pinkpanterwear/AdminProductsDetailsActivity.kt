package com.example.slickkwear

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pinkpanterwear.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AdminProductsDetailsActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var productImage: ShapeableImageView? = null
    private var deleteProductButton: Button? = null
    private var editProductButton: Button? = null
    private var productName: MaterialTextView? = null
    private var productPrice: MaterialTextView? = null
    private var productQuantity: MaterialTextView? = null
    private val ProductAvailability: MaterialTextView? = null
    private var productDescription: MaterialTextView? = null
    private var productID: String? = null

    private var productRef: FirebaseFirestore? = null
    private var productItem: DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_details)

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        productID = intent.getStringExtra("ProductUniqueID")

        productName = findViewById<View>(R.id.product_details_name) as MaterialTextView
        productPrice = findViewById<View>(R.id.product_details_price) as MaterialTextView
        productQuantity = findViewById<View>(R.id.product_details_quantity) as MaterialTextView
        productDescription =
            findViewById<View>(R.id.product_details_description) as MaterialTextView
        deleteProductButton = findViewById<View>(R.id.delete_product) as Button
        editProductButton = findViewById<View>(R.id.edit_product) as Button
        productImage = findViewById<View>(R.id.product_details_image) as ShapeableImageView


        getProductDetails(productID!!)
    }

    private fun getProductDetails(productID: String) {
        productRef = FirebaseFirestore.getInstance()
        productItem = productRef!!.collection("Products").document(productID)

        productItem!!.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    //                                    Products products = documentSnapshot.getValue(Products.class);
                    productName!!.text = documentSnapshot.getString("ProductName")
                    productPrice!!.text = documentSnapshot.getString("ProductPrice")
                    productDescription!!.text = documentSnapshot.getString("ProductDescription")
                    productQuantity!!.text = documentSnapshot.getString("ProductStatus")
                    Picasso.get().load(documentSnapshot.getString("ProductImage"))
                        .into(productImage)
                } else {
                    Toast.makeText(
                        this@AdminProductsDetailsActivity,
                        "Product Doesn't exist!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@AdminProductsDetailsActivity,
                    "Loading product details unsuccessful !",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}