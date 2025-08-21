package com.example.pinkpanterwear.repositories

import com.example.pinkpanterwear.entities.Wishlist
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    suspend fun createWishlist(name: String)
    fun getWishlists(): Flow<List<Wishlist>>
    suspend fun updateWishlistName(wishlistId: String, newName: String)
    suspend fun addProductToWishlist(wishlistId: String, productId: String)
    suspend fun removeProductFromWishlist(wishlistId: String, productId: String)
    suspend fun deleteWishlist(wishlistId: String)
}
