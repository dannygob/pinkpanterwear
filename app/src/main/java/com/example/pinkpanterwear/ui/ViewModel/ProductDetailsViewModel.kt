package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: com.example.pinkpanterwear.repositories.ProductRepository,
    private val cartRepository: com.example.pinkpanterwear.di.CartRepository,
    private val authHelper: AuthHelper,
) : ViewModel() {

    private val _productDetails = MutableStateFlow<Product?>(null)
    val productDetails: StateFlow<Product?> = _productDetails.asStateFlow()

    // TODO: Integrate actual size data if API/backend supports it.
    private val _availableSizes =
        MutableStateFlow<List<String>>(listOf("S", "M", "L", "XL")) // Default/Placeholder
    val availableSizes: StateFlow<List<String>> = _availableSizes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProductById(productIdString: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val productIdInt = productIdString.toIntOrNull()
                if (productIdInt == null) {
                    _error.value = "Invalid product ID format."
                    _isLoading.value = false
                    return@launch
                }

                val product = productRepository.getProductById(productIdInt)
                if (product != null) {
                    _productDetails.value = product as Product?
                    // Potentially fetch/set real sizes if product object had them
                    // For now, _availableSizes uses a default or pre-set list.
                } else {
                    _error.value = "Product not found."
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch product details: ${e.message}"
                // Log.e("ProductDetailsVM", "Error loading product", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(
        product: Product,
        selectedSize: String?,
        quantity: Int
    ) { // Start of addToCart method body
        viewModelScope.launch {
            val userId = authHelper.getCurrentUser()?.uid
            if (userId.isNullOrEmpty()) {
                _error.value = "User not logged in. Cannot add to cart."
                // Log.w("ProductDetailsVM", "User not logged in, cannot add to cart")
                return@launch
            }

            // TODO: Handle selectedSize. Currently not stored in FirestoreCartItem or CartItem for simplicity.
            // If size needs to be stored, CartItem, FirestoreCartItem, and CartRepository methods would need updates.
            Log.d(
                "ProductDetailsVM",
                "Adding to cart: User: $userId, ProductID: ${product.id}, Qty: $quantity, Size: $selectedSize"
            )

            val success = cartRepository.addCartItem(userId, product, quantity)
            if (success) {
                // TODO: Expose a success message StateFlow for the Activity to observe (e.g., for a Toast)
                Log.i("ProductDetailsVM", "${product.name} added/updated in cart for user $userId.")
            } else {
                _error.value = "Failed to add item to cart. Please try again."
                // Log.e("ProductDetailsVM", "Failed to add/update \${product.name} in cart for user \$userId.")
            }
        }
    } // End of addToCart method body
}
