package com.example.pinkpanterwear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = ProductRepository() // TODO: Use dependency injection

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = repository.getAllProducts()
            } catch (e: Exception) {
                _error.value = "Failed to fetch products: ${e.message}"
                // Log.e("HomeViewModel", "Error fetching products", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
