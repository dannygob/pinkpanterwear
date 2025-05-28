package com.example.slickkwear

import android.R
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class UserProductsDetailsActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var productRef: FirebaseFirestore? = null
    private var productItem: DocumentReference? = null
    private var user_product_details_name: MaterialTextView? = null
    private var user_product_details_price: MaterialTextView? = null
    private var user_product_details_description: MaterialTextView? = null

    private var productImage: ShapeableImageView? = null

    private var productID: String? = null
    private var user_product_details_size: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products_details)

        toolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24)

        productID = intent.getStringExtra("ProductUniqueID")

        productImage = findViewById<View>(R.id.user_product_details_image) as ShapeableImageView
        user_product_details_name =
            findViewById<View>(R.id.user_product_details_name) as MaterialTextView
        user_product_details_price =
            findViewById<View>(R.id.user_product_details_price) as MaterialTextView
        user_product_details_description =
            findViewById<View>(R.id.user_product_details_description) as MaterialTextView

        user_product_details_size =
            findViewById<View>(R.id.user_product_details_size) as LinearLayout


        productSize()

        productRef = FirebaseFirestore.getInstance()

        getProductDetails(productID!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                //Returns to the previous page
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getProductDetails(productID: String) {
        productItem = productRef!!.collection("Products").document(productID)
        productItem!!.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Picasso.get().load(documentSnapshot.getString("ProductImage"))
                        .into(productImage)
                    user_product_details_name!!.text = documentSnapshot.getString("ProductName")
                    user_product_details_price!!.text =
                        "Ksh " + (documentSnapshot.getLong("ProductPrice").toString())
                    user_product_details_description!!.text =
                        documentSnapshot.getString("ProductDescription")
                } else {
                    Toast.makeText(
                        this@UserProductsDetailsActivity,
                        "Product doesn't exists!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun productSize() {
        for (i in 36..42) {
//            String t_id = "val"+i;
            val valueTV = TextView(applicationContext)
            //            valueTV.setText("36"+i);
            valueTV.text =
                Html.fromHtml("<strike><font color=\'#757575\'>$i</font></strike>")
            valueTV.id = ("102$i").toInt()
            valueTV.setPadding(15, 15, 15, 15)
            valueTV.setBackgroundResource(R.drawable.user_product_details_size_bg)
            valueTV.gravity = Gravity.CENTER
            //            valueTV.setBackgroundColor(Color.GRAY);
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(5, 5, 5, 5)
            valueTV.layoutParams = params

            user_product_details_size!!.addView(valueTV)
        }
    }
}