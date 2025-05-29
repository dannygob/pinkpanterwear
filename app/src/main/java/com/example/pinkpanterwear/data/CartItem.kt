package com.example.pinkpanterwear.data

data class CartItem(
    val product: Product,
    var quantity: Int = 1
    val size: String? = null
)