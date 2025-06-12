package com.example.pinkpanterwear.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminProductsViewModel : ViewModel() {

    private val repository = ProductRepository() // TODO: Dependency Injection

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _actionFeedback = MutableStateFlow<String?>(null) // For delete status
    val actionFeedback: StateFlow<String?> = _actionFeedback.asStateFlow()

    init {
        fetchAdminProducts()
    }

    fun fetchAdminProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = repository.getAllProductsFromFirestore()
            } catch (e: Exception) {
                Log.e("AdminProductsVM", "Error fetching products", e)
                _error.value = "Failed to fetch products: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            // Consider adding a specific loading state for delete action if it's slow
            val success = repository.deleteProduct(productId)
            if (success) {
                _actionFeedback.value = "Product deleted successfully."
                fetchAdminProducts() // Refresh the list
            } else {
                _actionFeedback.value = "Failed to delete product."
            }
        }
    }

    fun consumeActionFeedback() {
        _actionFeedback.value = null
    }
}
