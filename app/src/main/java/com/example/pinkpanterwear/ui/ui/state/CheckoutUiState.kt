package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.entities.CartItem

sealed interface CheckoutUiState {

    object Loading : CheckoutUiState

    object LoggedOut : CheckoutUiState

    data class Summary(
        val items: List<CartItem>,
        val grandTotal: Double,
        val shippingAddress: Map<String, String>? = null
    ) : CheckoutUiState

    data class OrderPlaced(
        val orderId: String
    ) : CheckoutUiState

    data class Error(
        val message: String
    ) : CheckoutUiState
}
``
