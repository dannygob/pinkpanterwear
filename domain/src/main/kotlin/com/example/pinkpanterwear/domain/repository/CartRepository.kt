package com.example.pinkpanterwear.domain.repository

import com.example.pinkpanterwear.domain.entities.CartItem
import com.example.pinkpanterwear.domain.entities.Product

interface CartRepository {
    suspend fun getCartItems(userId: String): List<CartItem>
    suspend fun addProductToCart(userId: String, product: Product, quantity: Int, size: String?): Result<Unit>
    suspend fun updateCartItemQuantity(userId: String, productId: Int, newQuantity: Int): Result<Unit>
    suspend fun removeProductFromCart(userId: String, productId: Int): Result<Unit>
    suspend fun clearCart(userId: String): Result<Unit>
}
