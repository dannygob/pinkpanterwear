package com.example.pinkpanterwear.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pinkpanterwear.data.local.database.dao.CartDao
import com.example.pinkpanterwear.data.local.database.dao.WishlistDao
import com.example.pinkpanterwear.data.local.database.entities.CartItemDbo
import com.example.pinkpanterwear.data.local.database.entities.WishlistItemDbo

@Database(
    entities = [CartItemDbo::class, WishlistItemDbo::class],
    version = 1,
    exportSchema = false // Set to true if you want to export schema for migrations
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao

    // Companion object for singleton instance if not using Hilt to provide it,
    // but Hilt will handle providing the instance.
    // No companion object needed here for providing instance.
}
