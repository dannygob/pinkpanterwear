package com.example.pinkpanterwear.domain.entities

data class Wishlist(
    val id: String,
    val name: String,
    val productIds: List<String> = emptyList()
)