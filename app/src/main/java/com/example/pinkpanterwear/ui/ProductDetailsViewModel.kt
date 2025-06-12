package com.example.pinkpanterwear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.CartItem
import com.example.pinkpanterwear.data.CartRepository
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception // For general exception catching

class ProductDetailsViewModel : ViewModel() {

    // TODO: Use dependency injection for repositories
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository() // Assuming CartRepository exists and is set up

    private val _productDetails = MutableStateFlow<Product?>(null)
    val productDetails: StateFlow<Product?> = _productDetails.asStateFlow()

    // TODO: Integrate actual size data if API/backend supports it.
    private val _availableSizes = MutableStateFlow<List<String>>(listOf("S", "M", "L", "XL")) // Default/Placeholder
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
                    _productDetails.value = product
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

    fun addToCart(product: Product, selectedSize: String?, quantity: Int) {
        viewModelScope.launch {
            // TODO: Ensure CartRepository and CartItem can handle selectedSize if necessary
            val cartItem = CartItem(product, quantity /* add size here if CartItem model supports it */)
            // For now, assuming CartRepository().addCartItem() handles this.
            // cartRepository.addCartItem(cartItem) // This needs CartRepository to be fully implemented
            // Log.d("ProductDetailsVM", "Add to cart: \${product.name}, Qty: \$quantity, Size: \$selectedSize")
            // Placeholder for actual cart logic for now if CartRepository isn't ready for this subtask
        }
    }
}
