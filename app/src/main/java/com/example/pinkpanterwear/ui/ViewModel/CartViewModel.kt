package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.repositories.CartRepository
import com.example.pinkpanterwear.ui.state.CartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authHelper: AuthHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCartItems()
    }

    fun loadCartItems() {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _uiState.value = CartUiState.LoggedOut
            return
        }

        viewModelScope.launch {
            _uiState.value = CartUiState.Loading

            runCatching {
                cartRepository.getCartItems(userId)
            }.onSuccess { items ->
                _uiState.value = CartUiState.Content(items)
            }.onFailure {
                Log.e("CartViewModel", "Error loading cart", it)
                _uiState.value =
                    CartUiState.Error(it.message ?: "Failed to load cart")
            }
        }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Int) {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _uiState.value =
                CartUiState.ActionMessage("You must be logged in.")
            return
        }

        if (newQuantity < 0) {
            _uiState.value =
                CartUiState.ActionMessage("Quantity cannot be negative.")
            return
        }

        viewModelScope.launch {
            val success = cartRepository
                .updateItemQuantity(userId, productId, newQuantity)

            if (success) {
                loadCartItems()
                _uiState.value =
                    CartUiState.ActionMessage(
                        if (newQuantity > 0) "Quantity updated."
                        else "Item removed."
                    )
            } else {
                _uiState.value =
                    CartUiState.Error("Failed to update quantity.")
            }
        }
    }

    fun removeItem(productId: Int) {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _uiState.value =
                CartUiState.ActionMessage("You must be logged in.")
            return
        }

        viewModelScope.launch {
            val success = cartRepository.removeItem(userId, productId)
            if (success) {
                loadCartItems()
                _uiState.value =
                    CartUiState.ActionMessage("Item removed from cart.")
            } else {
                _uiState.value =
                    CartUiState.Error("Failed to remove item.")
            }
        }
    }

    fun clearCart() {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _uiState.value =
                CartUiState.ActionMessage("You must be logged in.")
            return
        }

        viewModelScope.launch {
            val result = cartRepository.clearCart(userId)
            _uiState.value =
                if (result.isSuccess) {
                    CartUiState.Content(emptyList())
                } else {
                    CartUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to clear cart"
                    )
                }
        }
    }
}
``
