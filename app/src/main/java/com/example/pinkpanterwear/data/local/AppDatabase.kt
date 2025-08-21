package com.example.pinkpanterwear.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pinkpanterwear.dao.CartDao
import com.example.pinkpanterwear.dao.WishlistDao
import com.example.pinkpanterwear.entities.CartItem
import com.example.pinkpanterwear.entities.WishlistItemDbo

@Database(entities = [CartItem::class, WishlistItemDbo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
}
