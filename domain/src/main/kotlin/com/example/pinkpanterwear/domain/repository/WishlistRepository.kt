package com.example.pinkpanterwear.domain.repository

// No direct import for Wishlist entity needed here anymore if we focus on product IDs for the list.
// Import Result or define a custom one if not using Kotlin's stdlib Result implicitly.

interface WishlistRepository {
    suspend fun getUserWishlistProductIds(userId: String): List<Int>
    suspend fun addProductToWishlist(userId: String, productId: Int): Result<Unit>
    suspend fun removeProductFromWishlist(userId: String, productId: Int): Result<Unit>
    suspend fun isProductInWishlist(userId: String, productId: Int): Boolean
}
