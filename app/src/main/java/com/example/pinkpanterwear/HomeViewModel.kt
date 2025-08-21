package com.example.pinkpanterwear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.usecase.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
    // private val getAllCategoriesUseCase: GetAllCategoriesUseCase // If home also shows categories
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Add _categories StateFlow if HomeViewModel also handles categories
    // private val _categories = MutableStateFlow<List<com.example.pinkpanterwear.domain.entities.Category>>(emptyList())
    // val categories: StateFlow<List<com.example.pinkpanterwear.domain.entities.Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false) // Consider separate loading states for products/categories
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null) // Consider separate error states
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchProducts()
        // fetchCategories() // If also handling categories
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true // Or a specific product loading state
            _error.value = null    // Or a specific product error state
            try {
                _products.value = getAllProductsUseCase()
            } catch (e: Exception) {
                _error.value = "Failed to fetch products: ${e.message}"
            } finally {
                _isLoading.value = false // Or a specific product loading state
            }
        }
    }

    // fun fetchCategories() { ... } // Implement if HomeViewModel handles categories
}