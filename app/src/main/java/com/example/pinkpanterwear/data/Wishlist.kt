package com.example.pinkpanterwear.data

data class Wishlist(
    val id: String,
    val name: String,
    val productIds: List<String> = emptyList()
)