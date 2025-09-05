package com.example.pink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var productUniqueID: String = "",
    var productName: String = "",
    var productImage: String = "",
    var productPrice: Double = 0.0,
    var quantity: Int = 1,
)