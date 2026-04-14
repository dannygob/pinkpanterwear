package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.CartItem

sealed interface CartUiState {

    object Loading : CartUiState

    object LoggedOut : CartUiState

    data class Content(
        val items: List<CartItem>
    ) : CartUiState

    data class ActionMessage(
        val message: String
    ) : CartUiState

    data class Error(
        val message: String
    ) : CartUiState
}
``
