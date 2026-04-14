package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.Product

sealed interface ProductDetailsUiState {

    object Loading : ProductDetailsUiState

    data class Content(
        val product: Product,
        val availableSizes: List<String> = listOf("S", "M", "L", "XL")
    ) : ProductDetailsUiState

    object NotFound : ProductDetailsUiState

    object LoggedOut : ProductDetailsUiState

    data class Error(val message: String) : ProductDetailsUiState
}
``
