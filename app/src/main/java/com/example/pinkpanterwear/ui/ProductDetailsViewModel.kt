package com.example.pinkpanterwear.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.data.CartItem
import com.example.pinkpanterwear.data.CartRepository
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {

    // TODO: Inject ProductRepository and CartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    private val _productDetails = MutableStateFlow<Product?>(null)
    val productDetails: StateFlow<Product?> = _productDetails

    private val _availableSizes = MutableStateFlow<List<String>>(emptyList())
    val availableSizes: StateFlow<List<String>> = _availableSizes

    fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _productDetails.value = productRepository.getProductDetails(productId)
            _availableSizes.value = productRepository.getProductSizes(productId)
        }
    }

    fun addToCart(product: Product, selectedSize: String?, quantity: Int) {
        viewModelScope.launch {
            val cartItem = CartItem(product, quantity) // Assuming CartItem can hold Product and quantity
            // TODO: You might need to adapt CartItem or the repository method
            // to include size if your cart data structure requires it.
            cartRepository.addCartItem(cartItem)
        }
    }
}