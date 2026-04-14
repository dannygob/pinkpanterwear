package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.repositories.ProductRepository
import com.example.pinkpanterwear.ui.state.AdminProductsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminProductsUiState>(AdminProductsUiState.Loading)
    val uiState: StateFlow<AdminProductsUiState> = _uiState.asStateFlow()

    init {
        fetchAdminProducts()
    }

    fun fetchAdminProducts() {
        viewModelScope.launch {
            _uiState.value = AdminProductsUiState.Loading

            runCatching {
                repository.getAllProducts()
            }.onSuccess { products ->
                _uiState.value =
                    AdminProductsUiState.Success(products)
            }.onFailure {
                Log.e("AdminProductsVM", "Error fetching products", it)
                _uiState.value =
                    AdminProductsUiState.Error(
                        it.message ?: "Failed to fetch products"
                    )
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            runCatching {
                repository.deleteProduct(productId)
            }.onSuccess { success ->
                if (success) {
                    _uiState.value =
                        AdminProductsUiState.ActionMessage(
                            "Product deleted successfully."
                        )
                    fetchAdminProducts()
                } else {
                    _uiState.value =
                        AdminProductsUiState.Error(
                            "Failed to delete product."
                        )
                }
            }.onFailure {
                Log.e("AdminProductsVM", "Error deleting product", it)
                _uiState.value =
                    AdminProductsUiState.Error(
                        it.message ?: "Delete error"
                    )
            }
        }
    }
}
``
