package com.example.pinkpanterwear.repository

import com.example.pinkpanterwear.dao.CartDao
import com.example.pinkpanterwear.entities.CartItem
import com.example.pinkpanterwear.entities.Product

import com.example.pinkpanterwear.repositories.CartRepository
import kotlinx.coroutines.flow.first // To get a single list from Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override suspend fun getCartItems(userId: String): List<CartItem> {
        val dboList = cartDao.getCartItems(userId).first() // Collect the first list emitted by Flow
        return dboList.map { dbo ->
            // Create a Product domain entity from DBO's denormalized fields
            val product = Product(
                id = dbo.productId,
                name = dbo.productName,
                price = dbo.productPrice,
                imageUrl = dbo.productImageUrl,
                description = "", // Not available in CartItem, use default or fetch if critical
                category = "",    // Not available in CartItem, use default or fetch if critical
                rating = null     // Not available in CartItem, use default or fetch if critical
            )
            CartItem(
                product = product,
                quantity = dbo.quantity,
                size = dbo.size,
                userId = TODO(),
                productId = TODO(),
                productName = TODO(),
                productPrice = TODO(),
                productImageUrl = TODO()
            )
        }
    }

    override suspend fun addProductToCart(userId: String, product: Product, quantity: Int, size: String?): Result<Unit> {
        return try {
            val existingDbo = cartDao.getCartItem(userId, product.id)
            if (existingDbo != null) {
                // Product exists, update quantity and potentially size
                val updatedDbo = existingDbo.copy(
                    quantity = existingDbo.quantity + quantity,
                    size = size ?: existingDbo.size // Keep old size if new one isn't provided, or update if new one is.
                                                    // Business logic might be more complex if size changes should mean a new item.
                )
                cartDao.updateCartItem(updatedDbo)
            } else {
                // New product, insert
                val newDbo = CartItemDbo(
                    userId = userId,
                    productId = product.id,
                    quantity = quantity,
                    size = size,
                    productName = product.name,
                    productPrice = product.price,
                    productImageUrl = product.imageUrl
                )
                cartDao.insertCartItem(newDbo)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCartItemQuantity(userId: String, productId: Int, newQuantity: Int): Result<Unit> {
        return try {
            val itemDbo = cartDao.getCartItem(userId, productId)
            if (itemDbo != null) {
                if (newQuantity > 0) {
                    cartDao.updateCartItem(itemDbo.copy(quantity = newQuantity))
                } else {
                    // If quantity becomes zero or less, remove the item from cart
                    cartDao.deleteCartItem(userId, productId)
                }
                Result.success(Unit)
            } else {
                 Result.failure(NoSuchElementException("Cart item not found for product ID: $productId, user ID: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeProductFromCart(userId: String, productId: Int): Result<Unit> {
        return try {
            cartDao.deleteCartItem(userId, productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearCart(userId: String): Result<Unit> {
        return try {
            cartDao.clearCart(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
