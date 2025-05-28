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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminCategoriesAddEditActivity : AppCompatActivity() {
    private var addCategoryName: TextInputLayout? = null
    private var addCategoryButton: MaterialButton? = null
    private var addCategoryImage: ShapeableImageView? = null
    private var loadingBar: ProgressDialog? = null

    private var categoryUniqueID: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var categoryName: String? = null
    private var downloadImageUrl: String? = null

    private var ImageUri: Uri? = null

    private var categoryImagesRef: StorageReference? = null
    private var categoryRef: FirebaseFirestore? = null

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories_add_edit)

        toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        //        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back_icon)

        categoryImagesRef = FirebaseStorage.getInstance().reference.child("Category Images")
        categoryRef = FirebaseFirestore.getInstance()

        addCategoryImage = findViewById<ShapeableImageView>(R.id.admin_add_category_image)
        addCategoryName = findViewById<TextInputLayout>(R.id.admin_add_category_name)
        addCategoryButton = findViewById<MaterialButton>(R.id.admin_add_category_btn)
        loadingBar = ProgressDialog(this)

        addCategoryImage.setOnClickListener(
            View.OnClickListener { openGallery() }
        )
        addCategoryButton.setOnClickListener(
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
            addCategoryImage!!.setImageURI(ImageUri)
        }
    }

    private fun validateProductData() {
        categoryName = addCategoryName!!.editText!!.text.toString()

        if (ImageUri == null) {
            Toast.makeText(this, "Category Image cannot be empty !", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Category name cannot be empty !", Toast.LENGTH_SHORT).show()
        } else {
            storeProductDetails()
        }
    }

    private fun storeProductDetails() {
        loadingBar!!.setTitle("Adding Category")
        loadingBar!!.setMessage("Please wait...")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()

        val calendar = Calendar.getInstance()

        @SuppressLint("SimpleDateFormat") val currentDate = SimpleDateFormat("yyyy/MM/dd")
        saveCurrentDate = currentDate.format(calendar.time)

        @SuppressLint("SimpleDateFormat") val currentTime = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calendar.time)

        categoryUniqueID = saveCurrentDate + saveCurrentTime
        categoryUniqueID = categoryUniqueID!!.replace("[^\\d]".toRegex(), "")

        val filepath =
            categoryImagesRef!!.child(ImageUri!!.lastPathSegment + categoryUniqueID + "jpg")
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
        val productMap = HashMap<String, Any?>()
        productMap["CategoryUniqueID"] = categoryUniqueID
        productMap["DateCreated"] = saveCurrentDate
        productMap["TimeCreated"] = saveCurrentTime
        productMap["CategoryName"] = categoryName
        productMap["CategoryImage"] = downloadImageUrl
        productMap["CategoryStatus"] = "active"
        productMap["CategoryDeleted"] = "false"

        categoryRef!!.collection("Categories").document(categoryUniqueID!!).set(productMap)
            .addOnSuccessListener {
                loadingBar!!.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Category Added Successfully.",
                    Toast.LENGTH_SHORT
                )
                    .show()

                val intent = Intent(
                    applicationContext,
                    AdminCategoriesActivity::class.java
                )
                startActivity(intent)
            }
            .addOnFailureListener {
                loadingBar!!.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Error!!!! Category not added.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    companion object {
        private const val GalleryPick = 1
    }
}