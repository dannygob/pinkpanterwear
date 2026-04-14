package com.example.pinkpanterwear.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.repositories.ProductRepository
import com.example.pinkpanterwear.ui.state.UserCategoryProductsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCategoryProductsViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UserCategoryProductsUiState>(
            UserCategoryProductsUiState.Loading
        )
    val uiState: StateFlow<UserCategoryProductsUiState> = _uiState.asStateFlow()

    fun fetchProductsForCategory(categoryName: String) {
        viewModelScope.launch {
            _uiState.value = UserCategoryProductsUiState.Loading

            runCatching {
                repository.getProductsByCategory(categoryName)
            }.onSuccess { products ->
                _uiState.value =
                    UserCategoryProductsUiState.Success(
                        category = categoryName,
                        products = products
                    )
            }.onFailure {
                _uiState.value =
                    UserCategoryProductsUiState.Error(
                        it.message
                            ?: "Error al cargar productos de $categoryName"
                    )
            }
        }
    }
}
``
