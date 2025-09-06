package com.example.pink.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
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
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class AdminCategoriesAddEditActivity : AppCompatActivity() {

    private lateinit var addCategoryName: TextInputLayout
    private lateinit var addCategoryButton: MaterialButton
    private lateinit var addCategoryImage: ShapeableImageView
    private lateinit var loadingBar: ProgressBar
    private lateinit var toolbar: Toolbar

    private var imageUri: Uri? = null
    private var downloadImageUrl: String? = null
    private var categoryUniqueID: String? = null
    private var categoryName: String? = null

    private val categoryImagesRef: StorageReference by lazy {
        FirebaseStorage.getInstance().getReference("Category Images")
    }

    private val categoryRef: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // ✅ Modern image picker launcher
    private val imagePickerLauncher =
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
        loadingBar = findViewById(R.id.progress_bar)

        addCategoryImage.setOnClickListener { openGallery() }
        addCategoryButton.setOnClickListener { validateCategoryData() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun openGallery() {
        imagePickerLauncher.launch("image/*")
    }

    private fun validateCategoryData() {
        categoryName = addCategoryName.editText?.text?.toString()

        when {
            imageUri == null -> showToast("Category image cannot be empty!")
            categoryName.isNullOrBlank() -> showToast("Category name cannot be empty!")
            else -> uploadCategoryImage()
        }
    }

    private fun uploadCategoryImage() {
        loadingBar.visibility = View.VISIBLE

        val calendar = Calendar.getInstance()
        val date = DateTimeFormatter.ofPattern("yyyyMMdd").format(calendar.time.toInstant())
        val time = DateTimeFormatter.ofPattern("HHmmss").format(calendar.time.toInstant())
        categoryUniqueID = "$date$time"

        val fileName = "${imageUri?.lastPathSegment}_${categoryUniqueID}.jpg"
        val filePath = categoryImagesRef.child(fileName)

        val uploadTask = filePath.putFile(imageUri!!)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) throw task.exception ?: Exception("Upload failed")
            filePath.downloadUrl
        }.addOnSuccessListener { uri ->
            downloadImageUrl = uri.toString()
            saveCategoryToDatabase()
        }.addOnFailureListener {
            loadingBar.visibility = View.GONE
            showToast("Image upload failed: ${it.message}")
        }
    }

    private fun saveCategoryToDatabase() {
        val now = Date()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val dateCreated = now.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            .format(dateFormatter)
        val timeCreated = now.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            .format(timeFormatter)

        val categoryData = mapOf(
            "CategoryUniqueID" to categoryUniqueID,
            "DateCreated" to dateCreated,
            "TimeCreated" to timeCreated,
            "CategoryName" to categoryName,
            "CategoryImage" to downloadImageUrl,
            "CategoryStatus" to "active",
            "CategoryDeleted" to "false"
        )

        categoryRef.collection("Categories")
            .document(categoryUniqueID!!)
            .set(categoryData)
            .addOnSuccessListener {
                loadingBar.visibility = View.GONE
                showToast("Category added successfully.")
                startActivity(Intent(this, AdminCategoriesActivity::class.java))
            }
            .addOnFailureListener {
                loadingBar.visibility = View.GONE
                showToast("Error adding category: ${it.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val GALLERY_PICK = 1 // 🎯 no longer used, just kept for reference
    }
}


