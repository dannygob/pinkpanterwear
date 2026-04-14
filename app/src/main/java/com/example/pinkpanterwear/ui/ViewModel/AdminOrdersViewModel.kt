package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.repositories.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminOrdersUiState>(AdminOrdersUiState.Loading)
    val uiState: StateFlow<AdminOrdersUiState> = _uiState.asStateFlow()

    init {
        fetchAllOrders()
    }

    fun fetchAllOrders() {
        viewModelScope.launch {
            _uiState.value = AdminOrdersUiState.Loading

            runCatching {
                orderRepository.getAllOrders()
            }.onSuccess { orders ->
                _uiState.value = AdminOrdersUiState.Success(orders)

                if (orders.isEmpty()) {
                    Log.i(
                        "AdminOrdersViewModel",
                        "No orders found."
                    )
                }
            }.onFailure { throwable ->
                Log.e(
                    "AdminOrdersViewModel",
                    "Error fetching all orders",
                    throwable
                )
                _uiState.value = AdminOrdersUiState.Error(
                    throwable.message ?: "Unknown error"
                )
            }
        }
    }
}
``
