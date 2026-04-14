package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.ProductRepository
import com.example.pinkpanterwear.ui.state.AdminProductAddEditUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AdminProductAddEditViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminProductAddEditUiState>(AdminProductAddEditUiState.Loading)
    val uiState: StateFlow<AdminProductAddEditUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadProductForEdit(productId: Int) {
        viewModelScope.launch {
            _uiState.value = AdminProductAddEditUiState.Loading

            runCatching {
                repository.getProductById(productId)
            }.onSuccess { product ->
                if (product == null) {
                    _uiState.value =
                        AdminProductAddEditUiState.Error("Product not found.")
                } else {
                    val categories =
                        (uiState.value as? AdminProductAddEditUiState.Content)
                            ?.categories
                            ?: emptyList()

                    _uiState.value =
                        AdminProductAddEditUiState.Content(
                            product = product,
                            categories = categories
                        )
                }
            }.onFailure {
                Log.e("AdminAddEditVM", "Error loading product", it)
                _uiState.value =
                    AdminProductAddEditUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            runCatching {
                repository.getAllCategoriesFromFirestore()
            }.onSuccess { categories ->
                _uiState.value =
                    AdminProductAddEditUiState.Content(
                        product = null,
                        categories = categories
                    )
            }.onFailure {
                Log.e("AdminAddEditVM", "Error loading categories", it)
                _uiState.value =
                    AdminProductAddEditUiState.Error(it.message ?: "Error loading categories")
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
        if (name.isBlank() || description.isBlank() || priceStr.isBlank()
            || category.isBlank() || imageUrl.isBlank()
        ) {
            _uiState.value =
                AdminProductAddEditUiState.Error("All fields must be filled.")
            return
        }

        val price = priceStr.toDoubleOrNull()
        if (price == null || price <= 0) {
            _uiState.value =
                AdminProductAddEditUiState.Error("Invalid price.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminProductAddEditUiState.Loading

            val product = Product(
                id = currentProductId ?: Random.nextInt(1, Int.MAX_VALUE),
                name = name,
                price = price,
                description = description,
                category = category,
                imageUrl = imageUrl,
                rating = null
            )

            val success = runCatching {
                if (currentProductId == null)
                    repository.addProduct(product)
                else
                    repository.updateProduct(product)
            }.getOrElse { false }

            _uiState.value =
                if (success) {
                    AdminProductAddEditUiState.SaveSuccess
                } else {
                    AdminProductAddEditUiState.Error("Failed to save product.")
                }
        }
    }
}
``
