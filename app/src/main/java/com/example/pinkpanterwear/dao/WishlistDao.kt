package com.example.pinkpanterwear.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pinkpanterwear.entities.WishlistItemDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items WHERE userId = :userId")
    fun getWishlistItems(userId: String): Flow<List<WishlistItemDbo>>

    // Alternatively, if only product IDs are needed as per revised thoughts:
    // @Query("SELECT productId FROM wishlist_items WHERE userId = :userId")
    // fun getWishlistItemProductIds(userId: String): Flow<List<Int>>

    @Query("SELECT * FROM wishlist_items WHERE userId = :userId AND productId = :productId")
    suspend fun getWishlistItem(userId: String, productId: Int): WishlistItemDbo?

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore if already in wishlist
    suspend fun insertWishlistItem(item: WishlistItemDbo)

    @Query("DELETE FROM wishlist_items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteWishlistItem(userId: String, productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE userId = :userId AND productId = :productId)")
    suspend fun isProductInWishlist(userId: String, productId: Int): Boolean
}
