package com.example.pinkpanterwear.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "wishlist_items", primaryKeys = ["userId", "productId"])
data class WishlistItemDbo(
    val userId: String,
    val productId: Int,
    val dateAdded: Long // Timestamp, e.g., System.currentTimeMillis()
)
