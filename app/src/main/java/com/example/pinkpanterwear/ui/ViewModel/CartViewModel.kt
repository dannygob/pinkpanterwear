package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    // TODO: Use dependency injection
    private val cartRepository = CartRepository()
    private val authHelper = AuthHelper()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // To inform UI about single action results e.g. item removal failed
    private val _actionFeedback = MutableStateFlow<String?>(null)
    val actionFeedback: StateFlow<String?> = _actionFeedback.asStateFlow()


    init {
        loadCartItems()
    }

    fun loadCartItems() {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _cartItems.value = emptyList() // Clear cart for logged-out user
            // _error.value = "Please log in to view your cart." // Or handle this in UI based on auth state
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _cartItems.value = cartRepository.getCartItems(userId)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error loading cart items", e)
                _error.value = "Failed to load cart: ${e.message}"
                _cartItems.value = emptyList() // Clear cart on error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Int) {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _actionFeedback.value = "You must be logged in to update your cart."
            return
        }
        if (newQuantity < 0) { // Allow 0 to effectively remove, repo handles it
            _actionFeedback.value = "Quantity cannot be negative."
            return
        }

        viewModelScope.launch {
            // No loading indicator for quick updates, or add one if preferred
            val success = cartRepository.updateItemQuantity(userId, productId, newQuantity)
            if (success) {
                loadCartItems() // Refresh cart from repository
                _actionFeedback.value =
                    if (newQuantity > 0) "Quantity updated." else "Item removed."
            } else {
                _actionFeedback.value = "Failed to update quantity."
            }
        }
    }

    fun removeItem(productId: Int) {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _actionFeedback.value = "You must be logged in to update your cart."
            return
        }
        viewModelScope.launch {
            val success = cartRepository.removeItem(userId, productId)
            if (success) {
                loadCartItems() // Refresh cart
                _actionFeedback.value = "Item removed from cart."
            } else {
                _actionFeedback.value = "Failed to remove item."
            }
        }
    }

    fun clearCart() {
        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _actionFeedback.value = "You must be logged in to clear your cart."
            return
        }
        viewModelScope.launch {
            val success = cartRepository.clearCart(userId)
            if (success) {
                _cartItems.value = emptyList() // Optimistically update UI
                _actionFeedback.value = "Cart cleared."
            } else {
                _actionFeedback.value = "Failed to clear cart."
            }
        }
    }

    // Call this to clear any displayed feedback message from the UI
    fun consumeActionFeedback() {
        _actionFeedback.value = null
    }
}