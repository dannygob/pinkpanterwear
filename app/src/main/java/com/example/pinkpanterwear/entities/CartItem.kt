package com.example.pinkpanterwear.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "cart_items",
    primaryKeys = ["userId", "productId"],
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    val userId: String,
    val productId: Int,
    val quantity: Int,
    val size: String?, // Nullable if size is optional
    val productName: String, // Denormalized for easier cart display
    val productPrice: Double,
    val productImageUrl: String
)
