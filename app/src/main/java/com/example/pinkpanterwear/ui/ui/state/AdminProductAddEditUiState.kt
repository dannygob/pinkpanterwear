package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.Product

sealed interface AdminProductAddEditUiState {

    object Loading : AdminProductAddEditUiState

    data class Content(
        val product: Product? = null,
        val categories: List<String> = emptyList()
    ) : AdminProductAddEditUiState

    object SaveSuccess : AdminProductAddEditUiState

    data class Error(val message: String) : AdminProductAddEditUiState
}
``
