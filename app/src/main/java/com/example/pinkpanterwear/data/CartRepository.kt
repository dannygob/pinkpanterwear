package com.example.pinkpanterwear.data

import com.example.pinkpanterwear.data.CartItem

class CartRepository {

    private val cartItemsList = mutableListOf<CartItem>()

    /**
     * Returns the current list of items in the cart.
     */
    fun getCartItems(): List<CartItem> {
        return cartItemsList.toList() // Return a copy to prevent external modification
    }

    /**
     * Adds an item to the cart or updates the quantity if the product already exists.
     */
    fun addCartItem(item: CartItem) {
        val existingItem = cartItemsList.find { it.product.id == item.product.id }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            cartItemsList.add(item)
        }
    }

    /**
     * Updates the quantity of a specific cart item.
     */
    fun updateCartItemQuantity(itemId: String, quantity: Int) {
        cartItemsList.find { it.product.id == itemId }?.let { item ->
            if (quantity > 0) {
                item.quantity = quantity
            } else {
                // If quantity is 0 or less, remove the item
                cartItemsList.remove(item)
            }
        }
    }

    /**
     * Removes a cart item.
     */
    fun removeCartItem(itemId: String) {
        cartItemsList.removeAll { it.product.id == itemId }
    }
}