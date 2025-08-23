package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminProductAddEditViewModel : ViewModel() {

    private val repository =
        _root_ide_package_.com.example.pinkpanterwear.repositories.ProductRepository() // TODO: DI

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
                _productToEdit.value = repository.getProductByIdFromFirestore(productId)
                if (_productToEdit.value == null) {
                    _error.value = "Product not found for editing."
                }
            } catch (e: Exception) {
                Log.e("AdminAddEditVM", "Error loading product ${productId}", e)
                _error.value = "Failed to load product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // Could add loading/error states for categories if complex, for now direct
            try {
                _categories.value = repository.getAllCategoriesFromFirestore()
            } catch (e: Exception) {
                Log.e("AdminAddEditVM", "Error loading categories", e)
                _error.value =
                    "Failed to load categories: ${e.message}" // Or a separate error flow for categories
            }
        }
    }

    fun saveProduct(
        currentProductId: Int?, // Null if new product
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

            val productIdToSave: Int
            val isNewProduct: Boolean

            if (currentProductId == null || currentProductId == 0) {
                isNewProduct = true
                // CRUDE ID generation. NOT FOR PRODUCTION.
                // A robust system needs a unique ID strategy (e.g., Firestore counter, backend generated, admin input with validation).
                // For now, using timestamp. Risk of collision is low for testing but exists.
                // Max Int is 2,147,483,647. System.currentTimeMillis() is larger.
                // Taking last 9 digits of millis and hoping for the best for this dev step.
                // Or simply use a random positive Int.
                productIdToSave = (System.currentTimeMillis() % 1_000_000_000L).toInt()
                Log.d("AdminAddEditVM", "Generated new product ID: $productIdToSave")

            } else {
                isNewProduct = false
                productIdToSave = currentProductId
            }

            // Rating will be null for new/edited products by admin initially
            val product = Product(
                productIdToSave,
                name,
                description,
                price,
                imageUrl,
                category,
                rating = null
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
        _productToEdit.value = null // Clear product after successful save if it was an edit
    }
}