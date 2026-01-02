package com.example.pinkpanterwear.entities

data class OrderItem(
    var orderId: String = "",
    val productId: Int = 0,
    val productName: String = "",
    val quantity: Int = 0,
    val pricePerItem: Double = 0.0
) {
    constructor() : this("", 0, "", 0, 0.0)
}