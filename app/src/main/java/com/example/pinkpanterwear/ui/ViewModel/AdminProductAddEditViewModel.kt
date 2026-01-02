package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AdminProductAddEditViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _productToEdit = MutableStateFlow<Product?>(null)
    val productToEdit: StateFlow<Product?> = _productToEdit.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    init {
        loadCategories()
    }

    fun loadProductForEdit(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _productToEdit.value = repository.getProductById(productId)
                if (_productToEdit.value == null) {
                    _error.value = "Product not found for editing."
                }
            } catch (e: Exception) {
                Log.e("AdminAddEditVM", "Error loading product $productId", e)
                _error.value = "Failed to load product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = repository.getAllCategoriesFromFirestore()
            } catch (e: Exception) {
                Log.e("AdminAddEditVM", "Error loading categories", e)
                _error.value = "Failed to load categories: ${e.message}"
            }
        }
    }

    fun saveProduct(
        currentProductId: Int?,
        name: String,
        description: String,
        priceStr: String,
        imageUrl: String,
        category: String
    ) {
        if (name.isBlank() || description.isBlank() || priceStr.isBlank() || category.isBlank() || imageUrl.isBlank()) {
            _error.value = "All fields must be filled."
            return
        }
        val price = priceStr.toDoubleOrNull()
        if (price == null || price <= 0) {
            _error.value = "Invalid price."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _saveSuccess.value = false

            val productIdToSave = currentProductId ?: Random.nextInt(1, Int.MAX_VALUE)
            val isNewProduct = currentProductId == null

            val product = Product(
                id = productIdToSave,
                name = name,
                price = price,
                description = description,
                category = category,
                imageUrl = imageUrl,
                rating = null // Admin cannot set rating
            )

            val success = if (isNewProduct) {
                repository.addProduct(product)
            } else {
                repository.updateProduct(product)
            }

            if (success) {
                _saveSuccess.value = true
            } else {
                _error.value =
                    if (isNewProduct) "Failed to add product." else "Failed to update product."
            }
            _isLoading.value = false
        }
    }

    fun consumeError() {
        _error.value = null
    }

    fun consumeSaveSuccess() {
        _saveSuccess.value = false
        _productToEdit.value = null
    }
}
