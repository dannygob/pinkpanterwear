package com.example.pinkpanterwear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView
import android.view.View
import android.view.LayoutInflater
import android.widget.EditText
import com.bumptech.glide.Glide
import android.text.InputFilter
import com.example.pinkpanterwear.data.CartItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.ui.ProductDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
// Import the Product data class (assuming it's in com.example.pinkpanterwear.data)
// Make sure you have the Product data class imported or defined
class UserProductDetailsActivity : AppCompatActivity() {

    private var selectedSize: String? = null
    private var selectedSizeTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products_details)

        // Retrieve product ID from intent extras
        val productId = intent.getStringExtra("PRODUCT_ID") ?: return // Handle missing ID

        // Get ViewModel
        val viewModel: ProductDetailsViewModel by viewModels()

        // Load product details
        viewModel.loadProduct(productId)

        val productNameTextView: TextView = findViewById(R.id.user_product_details_name)
        val productPriceTextView: TextView = findViewById(R.id.user_product_details_price)
        val productDescriptionTextView: TextView = findViewById(R.id.user_product_details_description)
        val productImageview: ImageView = findViewById(R.id.user_product_details_image)
        val decreaseQuantityButton: Button = findViewById(R.id.decrease_quantity_button)
        val quantityEditText: EditText = findViewById(R.id.quantity_edit_text)
        val increaseQuantityButton: Button = findViewById(R.id.increase_quantity_button)
        val addToCartButton: Button = findViewById(R.id.add_to_cart_button)

        // Add InputFilter to allow only digits in quantityEditText
        quantityEditText.filters = arrayOf<InputFilter>(InputFilter.Digits())

        // Observe product details
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productDetails.collectLatest { product ->
                    if (product != null) {
                        productNameTextView.text = product.name
                        productPriceTextView.text = "$${product.price}" // Format as currency
                        productDescriptionTextView.text = product.description
                        Glide.with(this@UserProductDetailsActivity).load(product.imageUrl).into(productImageview)
                    } else {
                        // Handle case where product is not found
                        Toast.makeText(this@UserProductDetailsActivity, "Product not found", Toast.LENGTH_SHORT).show()
                        finish() // Optionally close the activity
                    }
                }
            }
        }

        // Observe available sizes
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.availableSizes.collectLatest { sizes ->
                    displayProductSizes(sizes)
                }
            }
        }

        // Set up quantity buttons
        decreaseQuantityButton.setOnClickListener {
            var quantity = quantityEditText.text.toString().toIntOrNull() ?: 1
            if (quantity > 1) {
                quantity--
                quantityEditText.setText(quantity.toString())
            }
        }

        increaseQuantityButton.setOnClickListener {
            var quantity = quantityEditText.text.toString().toIntOrNull() ?: 1
            quantity++
            quantityEditText.setText(quantity.toString())
        }

        // Set up Add to Cart button
        addToCartButton.setOnClickListener {
            val quantityString = quantityEditText.text.toString()
            val quantity = quantityString.toIntOrNull()

            if (quantity == null || quantity <= 0) {
                Toast.makeText(this, "Please enter a valid quantity (at least 1)", Toast.LENGTH_SHORT).show()
                quantityEditText.setText("1") // Reset to 1
                return@setOnClickListener
            }

            val currentProduct = viewModel.productDetails.value

            if (currentProduct != null) {
                if (selectedSize == null && viewModel.availableSizes.value.isNotEmpty()) {
                    Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Stop if size is required but not selected
                }
                viewModel.addToCart(currentProduct, selectedSize, quantity)
                Toast.makeText(this, "${currentProduct.name} added to cart!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error adding item to cart. Product details not loaded.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayProductSizes(sizes: List<String>) {
        val sizesLayout: LinearLayout = findViewById(R.id.user_product_details_size)
        sizesLayout.removeAllViews() // Clear previous size views
        selectedSizeTextView = null // Reset selected size TV when sizes are redisplayed
        selectedSize = null // Reset selected size string

        for (size in sizes) {
            val sizeTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { // Use apply for LayoutParams
                    setMargins(16, 8, 16, 8) // Add margins
                }
                text = size
                // TODO: Define background drawables (e.g., R.drawable.bg_size_unselected)
                setBackgroundResource(R.drawable.size_indicator_background) // Use the drawable selector
                setPadding(16, 8, 16, 8)
                isClickable = true
                focusable = View.FOCUSABLE
            }

            sizeTextView.setOnClickListener { clickedView ->
                // If there was a previously selected TextView, deselect it
                if (selectedSizeTextView != null && selectedSizeTextView != clickedView) {
                    selectedSizeTextView?.isSelected = false
                }
                // Set the clicked TextView as selected
                clickedView.isSelected = true

                // Update the selectedSize and selectedSizeTextView variables
                selectedSize = sizeTextView.text.toString()
                Toast.makeText(this, "Selected size: $selectedSize", Toast.LENGTH_SHORT).show()
                selectedSizeTextView = clickedView as TextView // Store the clicked TextView
            }
            sizesLayout.addView(sizeTextView)
        }
    }
}