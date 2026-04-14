package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.Product

sealed interface UserCategoryProductsUiState {

    object Loading : UserCategoryProductsUiState

    data class Success(
        val category: String,
        val products: List<Product>
    ) : UserCategoryProductsUiState

    data class Error(
        val message: String
    ) : UserCategoryProductsUiState
}
``
