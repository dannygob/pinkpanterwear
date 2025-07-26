package com.example.pink.repositories

import com.example.pink.dao.CartDao
import com.example.pink.model.CartItem

class CartRepository(private val cartDao: CartDao) {

    fun getAllItems() = cartDao.getAllItems()

    fun getTotalPrice() = cartDao.getTotalPrice()

    suspend fun insertItem(item: CartItem) = cartDao.insertItem(item)

    suspend fun updateItem(item: CartItem) = cartDao.updateItem(item)

    suspend fun deleteItem(item: CartItem) = cartDao.deleteItem(item)

    suspend fun clearCart() = cartDao.clearCart()
}