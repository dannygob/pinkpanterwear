package com.example.pinkpanterwear.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pinkpanterwear.entities.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM Cart_Items WHERE userId = :userId")
    fun getCartItems(userId: String): Flow<List<CartItem>>

    @Query("SELECT * FROM Cart_Items WHERE userId = :userId AND productId = :productId")
    suspend fun getCartItem(userId: String, productId: Int): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Query("DELETE FROM Cart_Items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteCartItem(userId: String, productId: Int)

    @Query("DELETE FROM Cart_Items WHERE userId = :userId")
    suspend fun clearCart(userId: String)
}
