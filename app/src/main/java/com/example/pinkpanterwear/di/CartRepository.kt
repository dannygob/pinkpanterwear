package com.example.pinkpanterwear.di

import com.example.pinkpanterwear.entities.CartItem
import com.example.pinkpanterwear.entities.Product


interface CartRepository {
    suspend fun getCartItems(userId: String): List<CartItem>
    suspend fun addCartItem(userId: String, product: Product, quantity: Int): Boolean
    suspend fun updateItemQuantity(userId: String, productId: Int, newQuantity: Int): Boolean
    suspend fun removeItem(userId: String, productId: Int): Boolean
    suspend fun clearCart(userId: String): Result<Unit>
}
