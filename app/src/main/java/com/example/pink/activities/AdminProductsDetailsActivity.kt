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

    private var productID: String? = null
    private lateinit var productRef: FirebaseFirestore

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

        getProductDetails(productID)
    }

    private fun getProductDetails(productID: String?) {
        if (productID.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.product_not_specified), Toast.LENGTH_SHORT)
                .show()
            return
        }

        productRef = FirebaseFirestore.getInstance()
        val productItem = productRef.collection("Products").document(productID)

        productItem.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    productName.text = documentSnapshot.getString("ProductName")
                    productPrice.text =
                        "${getString(R.string.currency)} ${documentSnapshot.getString("ProductPrice")}"
                    productDescription.text = documentSnapshot.getString("ProductDescription")
                    productQuantity.text = documentSnapshot.getString("ProductStatus")
                    Picasso.get().load(documentSnapshot.getString("ProductImage"))
                        .into(productImage)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.product_does_not_exist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, getString(R.string.could_not_load_product), Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
