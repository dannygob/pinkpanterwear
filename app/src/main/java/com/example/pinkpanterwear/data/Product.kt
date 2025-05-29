package com.example.pinkpanterwear.data

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val categoryId: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)