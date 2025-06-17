package com.example.pinkpanterwear.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pinkpanterwear.data.local.database.entities.CartItemDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: String): Flow<List<CartItemDbo>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun getCartItem(userId: String, productId: Int): CartItemDbo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemDbo)

    @Update
    suspend fun updateCartItem(item: CartItemDbo)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteCartItem(userId: String, productId: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: String)
}
