package com.example.pinkpanterwear.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Order(
    @DocumentId
    var orderId: String = "",
    val userId: String = "",
    val userName: String = "",
    val totalAmount: Double = 0.0,
    val orderStatus: String = "Pending",
    val orderDate: Timestamp? = null,
    val shippingAddress: Map<String, String> = emptyMap()
) {
    constructor() : this("", "", "", 0.0, "Pending", null, emptyMap())
}