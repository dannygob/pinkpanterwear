package com.example.pinkpanterwear.domain.entities

data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    val size: String? = null
)