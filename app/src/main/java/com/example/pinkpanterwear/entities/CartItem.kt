package com.example.pinkpanterwear.entities

import androidx.room.Entity

// No ForeignKey to ProductDbo yet, as ProductDbo isn't defined.
// We'll assume product details are either denormalized or fetched separately.

@Entity(tableName = "cart_items", primaryKeys = ["userId", "productId"])
data class CartItem(
    val userId: String,
    val productId: Int,
    val quantity: Int,
    val size: String?, // Nullable if size is optional
    // Optional: Denormalized product fields for easier cart display
    val productName: String,
    val productPrice: Double,
    val productImageUrl: String
)
