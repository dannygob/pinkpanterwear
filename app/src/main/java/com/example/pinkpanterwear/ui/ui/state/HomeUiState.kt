package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.Product

sealed interface HomeUiState {

    object Loading : HomeUiState

    data class Success(
        val products: List<Product>
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}
