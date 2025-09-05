package com.example.pink.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pink.model.CartItem
import com.example.pink.repositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    // 🛒 Lista de ítems en el carrito
    val cartItems: LiveData<List<CartItem>> = repository.getAllItems()

    // 💰 Precio total calculado en tiempo real
    val totalPrice: LiveData<Double> = repository.getTotalPrice()

    // ➕ Agregar producto al carrito
    fun addItem(item: CartItem) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    // ❌ Eliminar producto del carrito
    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    // 🔄 Actualizar cantidad u otros datos
    fun updateItem(item: CartItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    // 🧹 Vaciar el carrito
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // 🧮 Calcular total manualmente (opcional)
    fun getTotal(items: List<CartItem>): Double {
        return items.sumOf { it.productPrice * it.quantity }
    }

    // 🧠 Factory para inyección de dependencias
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