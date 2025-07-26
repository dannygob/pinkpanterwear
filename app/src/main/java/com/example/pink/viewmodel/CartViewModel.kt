package com.example.pink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pink.model.CartItem
import com.example.pink.repositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val cartItems = repository.getAllItems()
    val totalPrice = repository.getTotalPrice()

    fun addItem(item: CartItem) {
        viewModelScope.launch { repository.insertItem(item) }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch { repository.deleteItem(item) }
    }

    fun updateItem(item: CartItem) {
        viewModelScope.launch { repository.updateItem(item) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() }
    }
}