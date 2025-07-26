package com.example.pink.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class adminCategoriesAddEditActivity : AppCompatActivity() {

    private lateinit var addCategoryName: TextInputLayout
    private lateinit var addCategoryButton: MaterialButton
    private lateinit var addCategoryImage: ShapeableImageView
    private lateinit var loadingBar: ProgressDialog
    private lateinit var toolbar: Toolbar

    private var imageUri: Uri? = null
    private val categoryImagesRef: StorageReference =
        FirebaseStorage.getInstance().reference.child("Category Images")
    private val categoryRef: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                addCategoryImage.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories_add_edit)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_icon)

        addCategoryImage = findViewById(R.id.admin_add_category_image)
        addCategoryName = findViewById(R.id.admin_add_category_name)
        addCategoryButton = findViewById(R.id.admin_add_category_btn)
        loadingBar = ProgressDialog(this)

        addCategoryImage.setOnClickListener { openGallery() }
        addCategoryButton.setOnClickListener { validateProductData() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun validateProductData() {
        val categoryName = addCategoryName.editText?.text.toString()

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Category name cannot be empty!", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(this, "Select an image!", Toast.LENGTH_SHORT).show()
        } else {
            storeProductDetails(categoryName)
        }
    }

    private fun storeProductDetails(categoryName: String) {
        loadingBar.setTitle("Adding Category")
        loadingBar.setMessage("Please wait...")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val calendar = Calendar.getInstance()
                val currentDate =
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)
                val currentTime =
                    SimpleDateFormat("HHmmss", Locale.getDefault()).format(calendar.time)
                val categoryUniqueID = currentDate + currentTime

                val filepath =
                    categoryImagesRef.child("${imageUri?.lastPathSegment}$categoryUniqueID.jpg")
                val uploadTask = filepath.putFile(imageUri!!).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await().toString()

                val productMap = hashMapOf(
                    "CategoryUniqueID" to categoryUniqueID,
                    "DateCreated" to currentDate,
                    "TimeCreated" to currentTime,
                    "CategoryName" to categoryName,
                    "CategoryImage" to downloadUrl,
                    "CategoryStatus" to "active",
                    "CategoryDeleted" to "false"
                )

                categoryRef.collection("Categories").document(categoryUniqueID).set(productMap)
                    .await()

                withContext(Dispatchers.Main) {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this@adminCategoriesAddEditActivity,
                        "Category Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@adminCategoriesAddEditActivity,
                            adminCategoriesActivity::class.java
                        )
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadingBar.dismiss()
                    Toast.makeText(
                        this@adminCategoriesAddEditActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
