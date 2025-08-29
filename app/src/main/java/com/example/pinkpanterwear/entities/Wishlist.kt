package com.example.pinkpanterwear.entities

data class Wishlist(
    val id: String = "",
    val name: String = "",
    val productIds: List<*>? = emptyList(),
)
