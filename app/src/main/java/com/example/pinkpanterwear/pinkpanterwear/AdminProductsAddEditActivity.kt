package com.example.slickkwear

import android.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminProductsAddEditActivity : AppCompatActivity() {
    private var addProductName: TextInputLayout? = null
    private var addProductPrice: TextInputLayout? = null
    private var addProductDescription: TextInputLayout? = null
    private var addProductButton: MaterialButton? = null
    private var addProductImage: ShapeableImageView? = null
    private var addProductCategory: Spinner? = null
    private var loadingBar: ProgressDialog? = null

    private var productUniqueID: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var productPrice: String? = null
    private var productName: String? = null
    private var productDescription: String? = null
    private var productCategoryID: String? = null
    private var downloadImageUrl: String? = null

    private var ImageUri: Uri? = null

    private var productImagesRef: StorageReference? = null
    private var productRef: FirebaseFirestore? = null
    private var query: Query? = null

    private var toolbar: Toolbar? = null

    var categories: MutableList<SpinnerItems>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_add_edit)

        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        //        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_icon)

        productImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        productRef = FirebaseFirestore.getInstance()

        addProductImage = findViewById<ShapeableImageView>(R.id.admin_add_product_image)
        addProductName = findViewById<TextInputLayout>(R.id.admin_add_product_name)
        addProductPrice = findViewById<TextInputLayout>(R.id.admin_add_product_price)
        addProductDescription = findViewById<TextInputLayout>(R.id.admin_add_product_description)
        addProductCategory = findViewById<Spinner>(R.id.admin_add_product_category)
        loadingBar = ProgressDialog(this)

        addProductButton = findViewById<MaterialButton>(R.id.admin_add_product_btn)

        addProductImage.setOnClickListener(
            View.OnClickListener { openGallery() }
        )

        categorySpinner()

        addProductButton.setOnClickListener(
            View.OnClickListener { validateProductData() }
        )
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


    private fun categorySpinner() {
        // Spinner click listener
//        addProductCategory.setOnItemSelectedListener(this);

        query = productRef!!.collection("Categories")
        // Spinner Drop down elements
        categories = ArrayList()

        categories.add(SpinnerItems("Select category", ""))
        query.get().addOnSuccessListener { queryDocumentSnapshots ->
            for (result in queryDocumentSnapshots) {
                categories.add(
                    SpinnerItems(
                        result.getString("CategoryName"),
                        result.getString("CategoryUniqueID")
                    )
                )
            }
        }


        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        addProductCategory!!.adapter = dataAdapter
    }


    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, GalleryPick)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            addProductImage!!.setImageURI(ImageUri)
        }
    }


    private fun validateProductData() {
        productName = addProductName!!.editText!!.text.toString()
        productPrice = addProductPrice!!.editText!!.text.toString()
        productDescription = addProductDescription!!.editText!!.text.toString()

        val cat_position = addProductCategory!!.selectedItemPosition
        productCategoryID = categories!![cat_position].spinnerItemID



        if (ImageUri == null) {
            Toast.makeText(this, "Product Image cannot be empty !", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(productCategoryID)) {
            Toast.makeText(this, "Product Category cannot be empty !", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Product name cannot be empty !", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Product price cannot be empty !", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(this, "Product description cannot be empty !", Toast.LENGTH_SHORT).show()
        } else {
            storeProductDetails()
        }
    }

    private fun storeProductDetails() {
        loadingBar!!.setTitle("Adding Product")
        loadingBar!!.setMessage("Please wait...")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()

        val calendar = Calendar.getInstance()

        @SuppressLint("SimpleDateFormat") val currentDate = SimpleDateFormat("yyyy/MM/dd")
        saveCurrentDate = currentDate.format(calendar.time)

        @SuppressLint("SimpleDateFormat") val currentTime = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calendar.time)

        productUniqueID = saveCurrentDate + saveCurrentTime
        productUniqueID = productUniqueID!!.replace("[^\\d]".toRegex(), "")

        val filepath =
            productImagesRef!!.child(ImageUri!!.lastPathSegment + productUniqueID + "jpg")
        val uploadTask = filepath.putFile(ImageUri!!)

        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            //                        Toast.makeText(getApplicationContext(), "Product Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();
            val uriTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadImageUrl = filepath.downloadUrl.toString()
                filepath.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()
                    //                                            Toast.makeText(getApplicationContext(), "Getting Product Image Url Successfully.", Toast.LENGTH_SHORT).show();
                    saveProductInfoToDatabase()
                }
            }
        }
    }

    private fun saveProductInfoToDatabase() {
        val new_ProductPrice = productPrice!!.toInt()
        val productMap = HashMap<String, Any?>()
        productMap["ProductUniqueID"] = productUniqueID
        productMap["DateCreated"] = saveCurrentDate
        productMap["TimeCreated"] = saveCurrentTime
        productMap["ProductName"] = productName
        productMap["ProductImage"] = downloadImageUrl
        productMap["ProductPrice"] = new_ProductPrice
        productMap["ProductDescription"] = productDescription
        productMap["ProductCategory"] = productCategoryID
        productMap["ProductStatus"] = "active"

        productRef!!.collection("Products").document(productUniqueID!!).set(productMap)
            .addOnSuccessListener {
                loadingBar!!.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Product Added Successfully.",
                    Toast.LENGTH_SHORT
                )
                    .show()

                val intent = Intent(
                    applicationContext,
                    AdminProductsActivity::class.java
                )
                startActivity(intent)
            }
            .addOnFailureListener {
                loadingBar!!.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Error!!!! Product not added.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    companion object {
        private const val GalleryPick = 1
    }
}