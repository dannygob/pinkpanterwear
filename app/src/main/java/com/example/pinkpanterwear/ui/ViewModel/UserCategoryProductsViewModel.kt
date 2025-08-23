package com.example.pinkpanterwear.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCategoryProductsViewModel @Inject constructor(
    private val repository: com.example.pinkpanterwear.repositories.ProductRepository,
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchProductsForCategory(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = repository.getProductsByCategory(categoryName)
            } catch (e: Exception) {
                _error.value = "Failed to fetch products for category ${categoryName}: ${e.message}"
                // Log.e("UserCategoryProductsViewModel", "Error fetching products for category", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
