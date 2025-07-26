package com.example.pink.prevalent

import android.content.Context
import androidx.core.content.edit

object Prevalent {
    const val UserRememberMe: String = "UserRememberMe"
    const val UserLoggedIn: String = "UserLoggedIn"
    const val UserPhoneKey: String = "UserPhone"
    const val UserPasswordKey: String = "UserPassword"
    private var cartItems: Int = 0

    const val CART_ITEMS_KEY = "CartItems"

    fun getCartItemsCount(): Int {
        return cartItems
    }

    fun setCartItemsCount(count: Int) {
        cartItems = count
    }

    fun saveCartItems(context: Context, cartItems: Int) {
        val sharedPreferences = context.getSharedPreferences(UserRememberMe, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putInt(CART_ITEMS_KEY, cartItems)
        }
        setCartItemsCount(cartItems)
    }

    fun getCartItems(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(UserRememberMe, Context.MODE_PRIVATE)
        val count = sharedPreferences.getInt(CART_ITEMS_KEY, 0)
        setCartItemsCount(count)
        return count
    }
}
