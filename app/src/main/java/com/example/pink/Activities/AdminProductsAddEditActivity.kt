package com.example.pink.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pink.Fragment.SpinnerItems
import com.example.pink.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdminProductsAddEditActivity : AppCompatActivity() {

    private lateinit var addProductName: TextInputLayout
    private lateinit var addProductPrice: TextInputLayout
    private lateinit var addProductDescription: TextInputLayout
    private lateinit var addProductButton: MaterialButton
    private lateinit var addProductImage: ShapeableImageView
    private lateinit var addProductCategory: Spinner
    private lateinit var loadingBar: ProgressDialog
    private lateinit var toolbar: Toolbar

    private var imageUri: Uri? = null
    private val productImagesRef: StorageReference =
        FirebaseStorage.getInstance().reference.child("Product Images")
    private val productRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var categories: MutableList<SpinnerItems>

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                addProductImage.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_add_edit)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_icon)

        addProductImage = findViewById(R.id.admin_add_product_image)
        addProductName = findViewById(R.id.admin_add_product_name)
        addProductPrice = findViewById(R.id.admin_add_product_price)
        addProductDescription = findViewById(R.id.admin_add_product_description)
        addProductCategory = findViewById(R.id.admin_add_product_category)
        addProductButton = findViewById(R.id.admin_add_product_btn)
        loadingBar = ProgressDialog(this)

        addProductImage.setOnClickListener { openGallery() }
        addProductButton.setOnClickListener { validateProductData() }

        loadCategories()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun loadCategories() {
        categories = mutableListOf(SpinnerItems("Select category", ""))
        productRef.collection("Categories").get().addOnSuccessListener { snapshots ->
            for (result in snapshots) {
                categories.add(
                    SpinnerItems(
                        result.getString("CategoryName") ?: "",
                        result.getString("CategoryUniqueID") ?: ""
                    )
                )
            }
            val dataAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, categories)
            dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            addProductCategory.adapter = dataAdapter
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun validateProductData() {
        val productName = addProductName.editText?.text.toString()
        val productPrice = addProductPrice.editText?.text.toString()
        val productDescription = addProductDescription.editText?.text.toString()
        val selectedCategory = addProductCategory.selectedItem as SpinnerItems
        val productCategoryID = selectedCategory.spinnerItemID

        when {
            imageUri == null -> Toast.makeText(
                this,
                "Product Image cannot be empty!",
                Toast.LENGTH_SHORT
            ).show()

            productCategoryID == null || productCategoryID.isBlank() -> Toast.makeText(
                this,
                "Please select a category.",
                Toast.LENGTH_SHORT
            ).show()

            TextUtils.isEmpty(productName) -> addProductName.error = "Required!"
            TextUtils.isEmpty(productPrice) -> addProductPrice.error = "Required!"
            TextUtils.isEmpty(productDescription) -> addProductDescription.error = "Required!"
            else -> storeProductDetails(
                productName,
                productPrice,
                productDescription,
                productCategoryID
            )
        }
    }

    private fun storeProductDetails(
        productName: String,
        productPrice: String,
        productDescription: String,
        productCategoryID: String?,
    ) {
        loadingBar.setTitle("Adding Product")
        loadingBar.setMessage("Please wait...")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val calendar = Calendar.getInstance()
                val saveCurrentDate =
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
                val saveCurrentTime =
                    SimpleDateFormat("HHmmss", Locale.getDefault()).format(calendar.time)
                val productUniqueID = saveCurrentDate + saveCurrentTime

                val imageRef =
                    productImagesRef.child("${imageUri?.lastPathSegment}_${productUniqueID}.jpg")
                val uploadTask = imageRef.putFile(imageUri!!).await()
                val downloadImageUrl = uploadTask.storage.downloadUrl.await().toString()

                val productMap = mapOf(
                    "ProductUniqueID" to productUniqueID,
                    "DateCreated" to saveCurrentDate,
                    "TimeCreated" to saveCurrentTime,
                    "ProductName" to productName,
                    "ProductImage" to downloadImageUrl,
                    "ProductPrice" to productPrice.toIntOrNull(),
                    "ProductDescription" to productDescription,
                    "ProductCategory" to productCategoryID,
                    "ProductStatus" to "active"
                )

                productRef.collection("Products").document(productUniqueID).set(productMap).await()

                withContext(Dispatchers.Main) {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this@AdminProductsAddEditActivity,
                        "Product Added Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@AdminProductsAddEditActivity,
                            AdminProductsActivity::class.java
                        )
                    )
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this@AdminProductsAddEditActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
