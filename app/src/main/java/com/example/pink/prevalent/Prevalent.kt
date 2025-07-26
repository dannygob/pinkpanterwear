package com.example.pink.prevalent

import android.content.Context
import com.example.pink.model.Products
import io.paperdb.Paper

object Prevalent {
    const val UserRememberMe: String = "UserRememberMe"
    const val UserLoggedIn: String = "UserLoggedIn"
    const val UserPhoneKey: String = "UserPhone"
    const val UserPasswordKey: String = "UserPassword"
    private var cartItems: MutableList<Products> = ArrayList()

    const val CART_ITEMS_KEY = "CartItems"

    fun getCartItems(): MutableList<Products> {
        return cartItems
    }

    fun setCartItems(items: MutableList<Products>) {
        cartItems = items
    }

    fun getCartItemsCount(): Int {
        return cartItems.size
    }

    fun saveCartItems(context: Context) {
        Paper.init(context)
        Paper.book().write(CART_ITEMS_KEY, cartItems)
    }

    fun getCartItems(context: Context): MutableList<Products> {
        Paper.init(context)
        val savedItems = Paper.book().read<MutableList<Products>>(CART_ITEMS_KEY)
        if (savedItems != null) {
            cartItems = savedItems
        }
        return cartItems
    }

    fun addCartItem(product: Products) {
        cartItems.add(product)
    }

    fun removeCartItem(product: Products) {
        cartItems.remove(product)
    }
}

