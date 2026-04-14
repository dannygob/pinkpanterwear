package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.entities.OrderItem
import com.example.pinkpanterwear.repositories.CartRepository
import com.example.pinkpanterwear.repositories.OrderRepository
import com.example.pinkpanterwear.ui.state.CheckoutUiState
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val authHelper: AuthHelper
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun setShippingAddress(address: Map<String, String>) {
        val current = _uiState.value
        if (current is CheckoutUiState.Summary) {
            _uiState.value = current.copy(shippingAddress = address)
