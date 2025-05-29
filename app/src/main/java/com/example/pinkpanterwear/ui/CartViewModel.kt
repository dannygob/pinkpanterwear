package com.example.pinkpanterwear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.CartItem
import com.example.pinkpanterwear.data.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun loadCartItems() {
        _cartItems.value = cartRepository.getCartItems()
    }

    fun addCartItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.addCartItem(item)
            _cartItems.value = cartRepository.getCartItems() // Refresh list after adding
        }
    }

    fun updateItemQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(itemId, quantity)
            loadCartItems() // Refresh list after updating
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            cartRepository.removeCartItem(itemId)
            _cartItems.value = cartRepository.getCartItems() // Refresh list after removing
        }
    }
}