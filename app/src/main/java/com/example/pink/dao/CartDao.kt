package com.example.pink.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pink.model.CartItem

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem): Long

    @Update
    suspend fun updateItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart(): Int

    @Query("SELECT * FROM cart_items")
    fun getAllItems(): LiveData<List<CartItem>>

    @Query("SELECT SUM(productPrice * quantity) FROM cart_items")
    fun getTotalPrice(): LiveData<Double>

    @Query("SELECT * FROM cart_items WHERE productUniqueID = :productID LIMIT 1")
    suspend fun findItemByProductID(productID: String): CartItem?
}