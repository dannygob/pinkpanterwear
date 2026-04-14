package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.Product

sealed interface AdminProductsUiState {

    object Loading : AdminProductsUiState

    data class Success(
        val products: List<Product>
    ) : AdminProductsUiState

    data class ActionMessage(
        val message: String
    ) : AdminProductsUiState

    data class Error(
        val message: String
    ) : AdminProductsUiState
}
``
