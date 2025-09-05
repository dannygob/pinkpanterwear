package com.example.pink.repositories

import androidx.lifecycle.LiveData
import com.example.pink.dao.CartDao
import com.example.pink.model.CartItem

class CartRepository(private val cartDao: CartDao) {

    // 🛒 Obtener todos los ítems del carrito
    fun getAllItems(): LiveData<List<CartItem>> {
        return cartDao.getAllItems()
    }

    // 💰 Obtener el precio total del carrito
    fun getTotalPrice(): LiveData<Double> {
        return cartDao.getTotalPrice()
    }

    // ➕ Insertar un nuevo ítem
    suspend fun insertItem(item: CartItem) {
        val existingItem = cartDao.findItemByProductID(item.productUniqueID)
        if (existingItem != null) {
            existingItem.quantity += item.quantity
            cartDao.updateItem(existingItem)
        } else {
            cartDao.insertItem(item)
        }
    }

    // 🔄 Actualizar un ítem existente
    suspend fun updateItem(item: CartItem) {
        cartDao.updateItem(item)
    }

    // ❌ Eliminar un ítem
    suspend fun deleteItem(item: CartItem) {
        cartDao.deleteItem(item)
    }

    // 🧹 Vaciar el carrito
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}