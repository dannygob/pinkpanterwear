package com.example.pinkpanterwear.repository



import com.example.pinkpanterwear.dao.WishlistDao
import com.example.pinkpanterwear.entities.WishlistItemDbo
import com.example.pinkpanterwear.repositories.WishlistRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao
    // private val productRepository: ProductRepository // Would be needed for getUserWishlistProducts
) : WishlistRepository { // This will cause a compile error if WishlistRepository interface doesn't match

    // This method is not in the previously defined WishlistRepository interface.
    // It should be `getWishlist(userId: String): Wishlist?`
    // For now, creating as per current subtask's text.
    suspend fun getUserWishlistProductIds(userId: String): List<Int> {
        // DAO returns Flow<List<WishlistItemDbo>>. We need List<Int> (productIds).
        val dtoList = wishlistDao.getWishlistItems(userId).first()
        return dtoList.map { it.productId }
    }

    // This method's signature (productId: Int) differs from the interface (productId: String).
    // Creating as per current subtask's text.
    suspend fun addProductToWishlist(userId: String, productId: Int): Result<Unit> {
        return try {
            val wishlistItem = WishlistItemDbo(
                userId = userId,
                productId = productId,
                dateAdded = System.currentTimeMillis()
            )
            wishlistDao.insertWishlistItem(wishlistItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This method's signature (productId: Int) differs from the interface (productId: String).
    // Creating as per current subtask's text.
    suspend fun removeProductFromWishlist(userId: String, productId: Int): Result<Unit> {
        return try {
            wishlistDao.deleteWishlistItem(userId, productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // This method is not in the previously defined WishlistRepository interface.
    // Creating as per current subtask's text.
    suspend fun isProductInWishlist(userId: String, productId: Int): Boolean {
        // wishlistDao.isProductInWishlist already returns Boolean
        return try {
            wishlistDao.isProductInWishlist(userId, productId)
        } catch (e: Exception) {
            // Log error, default to false or rethrow depending on desired behavior
            false
        }
    }

    // The methods from the interface that are NOT implemented here by this exact text:
    // override suspend fun getWishlist(userId: String): Wishlist? { TODO() }
    // override suspend fun addProductIdToWishlist(userId: String, productId: String): Result<Unit> { TODO() }
    // override suspend fun removeProductIdFromWishlist(userId: String, productId: String): Result<Unit> { TODO() }


    // Example of how getUserWishlistProducts could be implemented if needed:
    // suspend fun getUserWishlistProducts(userId: String): List<Product> {
    //     val productIds = getUserWishlistProductIds(userId)
    //     return productIds.mapNotNull { productId ->
    //         try {
    //             productRepository.getProductById(productId) // Assuming productRepository takes Int
    //         } catch (e: Exception) {
    //             null // Or handle error
    //         }
    //     }
    // }
}
