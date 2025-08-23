package com.example.pinkpanterwear.ui.activities


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.ui.ViewModel.AdminProductAddEditViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminProductAddEditActivity : AppCompatActivity() {

    private val viewModel: AdminProductAddEditViewModel by viewModels()

    private lateinit var productImage: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imageUrlEditText: EditText // Added for image URL input
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar // Add to layout if not there

    private var currentProductId: Int? = null
    private var categoriesAdapter: ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_products_add_edit)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize views (IDs from activity_admin_products_add_edit.xml)
        productImage = findViewById(R.id.admin_add_product_image) // For URL preview
        categorySpinner = findViewById(R.id.admin_add_product_category)
        nameEditText = findViewById<TextInputLayout>(R.id.admin_add_product_name).editText!!
        priceEditText = findViewById<TextInputLayout>(R.id.admin_add_product_price).editText!!
        descriptionEditText =
            findViewById<TextInputLayout>(R.id.admin_add_product_description).editText!!
        imageUrlEditText =
            findViewById<TextInputLayout>(R.id.admin_add_product_image_url).editText!!


        saveButton = findViewById(R.id.admin_add_product_btn)
        // Assuming ProgressBar is added to the layout, else this will fail.
        // For now, let's assume it exists with id admin_add_edit_progress_bar
        try {
            progressBar = findViewById(R.id.admin_add_edit_progress_bar)
        } catch (e: Exception) {
            Log.e(
                "AdminAddEditActivity",
                "CRITICAL: ProgressBar R.id.admin_add_edit_progress_bar not found in XML."
            )
            progressBar = ProgressBar(this) // Dummy
        }


        currentProductId = intent.getIntExtra("PRODUCT_ID_INT", 0).let { if (it == 0) null else it }

        if (currentProductId != null) {
            supportActionBar?.title = "Edit Product"
            viewModel.loadProductForEdit(currentProductId!!)
        } else {
            supportActionBar?.title = "Add New Product"
        }

        setupCategorySpinner()
        observeViewModel()
        setupSaveButton()
    }

    private fun setupCategorySpinner() {
        categoriesAdapter =
            ArrayAdapter(this, R.layout.simple_spinner_item, mutableListOf<String>())
        categoriesAdapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoriesAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productToEdit.collectLatest { product ->
                    product?.let { populateFields(it) }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collectLatest { categoryList ->
                    categoriesAdapter?.clear()
                    categoriesAdapter?.addAll(categoryList)
                    categoriesAdapter?.notifyDataSetChanged()
                    // If editing, try to set spinner to product's category
                    viewModel.productToEdit.value?.category?.let { productCategory ->
                        val position = categoriesAdapter?.getPosition(productCategory)
                        if (position != null && position >= 0) {
                            categorySpinner.setSelection(position)
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { progressBar.isVisible = it }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { errorMsg ->
                    if (errorMsg != null) {
                        Toast.makeText(
                            this@AdminProductAddEditActivity,
                            errorMsg,
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.consumeError() // Consume error after showing
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveSuccess.collectLatest { success ->
                    if (success) {
                        Toast.makeText(
                            this@AdminProductAddEditActivity,
                            "Product saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumeSaveSuccess()
                        finish() // Go back to product list
                    }
                }
            }
        }
    }

    private fun populateFields(product: Product) {
        nameEditText.setText(product.name)
        priceEditText.setText(product.price.toString())
        descriptionEditText.setText(product.description)
        imageUrlEditText.setText(product.imageUrl) // Assumes this EditText exists

        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(productImage) // Preview current image

        val categoryPosition = categoriesAdapter?.getPosition(product.category)
        if (categoryPosition != null && categoryPosition >= 0) {
            categorySpinner.setSelection(categoryPosition)
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            viewModel.saveProduct(
                currentProductId = currentProductId,
                name = nameEditText.text.toString().trim(),
                description = descriptionEditText.text.toString().trim(),
                priceStr = priceEditText.text.toString().trim(),
                imageUrl = imageUrlEditText.text.toString().trim(), // Assumes this EditText exists
                category = categorySpinner.selectedItem?.toString() ?: ""
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
