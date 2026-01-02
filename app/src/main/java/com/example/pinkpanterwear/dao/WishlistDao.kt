package com.example.pinkpanterwear.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pinkpanterwear.entities.WishlistItemDbo
import com.example.pinkpanterwear.entities.WishlistItemWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query(
        """
        SELECT p.*, w.dateAdded 
        FROM wishlist_items w
        INNER JOIN products p ON w.productId = p.id
        WHERE w.userId = :userId
    """
    )
    fun getWishlistItemsWithProduct(userId: String): Flow<List<WishlistItemWithProduct>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWishlistItem(item: WishlistItemDbo)

    @Query("DELETE FROM wishlist_items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteWishlistItem(userId: String, productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE userId = :userId AND productId = :productId)")
    suspend fun isProductInWishlist(userId: String, productId: Int): Boolean
}
