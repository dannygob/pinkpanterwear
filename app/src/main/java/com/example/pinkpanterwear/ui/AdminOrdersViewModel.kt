package com.example.pinkpanterwear.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.Order
import com.example.pinkpanterwear.data.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminOrdersViewModel : ViewModel() {

    // TODO: Use dependency injection for OrderRepository
    private val orderRepository = OrderRepository()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchAllOrders()
    }

    fun fetchAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val fetchedOrders = orderRepository.getAllOrders()
                if (fetchedOrders.isEmpty()) {
                    // Could be a valid empty state, or an indication that no orders exist yet.
                    // For admin view, this is fine. Error state is for exceptions.
                    Log.i("AdminOrdersViewModel", "No orders found or repository returned empty list.")
                }
                _orders.value = fetchedOrders
            } catch (e: Exception) {
                Log.e("AdminOrdersViewModel", "Error fetching all orders", e)
                _error.value = "Failed to fetch orders: ${e.message}"
                _orders.value = emptyList() // Ensure list is empty on error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
