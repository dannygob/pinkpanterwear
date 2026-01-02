package com.example.pinkpanterwear.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Rating(
    val rate: Double = 0.0,
    val count: Int = 0
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    val imageUrl: String,
    val rating: String? = null // Storing as JSON string for simplicity with Room
)
