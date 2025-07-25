package com.example.pink.Prevalent

import android.content.Context
import androidx.core.content.edit

object Prevalent {
    const val UserRememberMe: String = "UserRememberMe"
    const val UserLoggedIn: String = "UserLoggedIn"
    const val UserPhoneKey: String = "UserPhone"
    const val UserPasswordKey: String = "UserPassword"
    var CartItems: Int = 0

    const val CART_ITEMS_KEY = "CartItems"

    fun saveCartItems(context: Context, cartItems: Int) {
        val sharedPreferences = context.getSharedPreferences(UserRememberMe, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putInt(CART_ITEMS_KEY, cartItems)
        }
    }

    fun getCartItems(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(UserRememberMe, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(CART_ITEMS_KEY, 0)
    }
}
