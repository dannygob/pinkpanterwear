package com.example.pink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pink.model.CartItem
import com.example.pink.repositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = repository.getAllItems()
    val totalPrice: LiveData<Double> = repository.getTotalPrice()

    fun addItem(item: CartItem) {
        viewModelScope.launch { repository.insertItem(item) }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch { repository.deleteItem(item) }
    }

    fun updateItem(item: CartItem) {
        viewModelScope.launch { repository.updateItem(item) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() }
    }

    // Puedes agregar función helper para calcular total manualmente si prefieres
    fun getTotal(items: List<CartItem>): Double {
        return items.sumOf { it.productPrice * it.quantity }
    }

    class Factory(private val repository: CartRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
