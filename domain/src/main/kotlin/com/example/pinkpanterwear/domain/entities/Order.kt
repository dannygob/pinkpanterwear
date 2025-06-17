package com.example.pinkpanterwear.domain.entities

import com.google.firebase.Timestamp // Correct import for Firebase Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Order(
    @DocumentId // Firestore will automatically populate this with the document ID
    var orderId: String = "", // Use var if @DocumentId is used, or manage manually

    val userId: String = "",
    val userName: String = "", // Denormalized for easier display in order lists
    val totalAmount: Double = 0.0,
    val orderStatus: String = "Pending", // e.g., "Pending", "Processing", "Shipped", "Delivered", "Cancelled"

    @ServerTimestamp // Firestore will set this on the server if null when writing
    val orderDate: Timestamp? = null,

    // Example: val shippingAddress: ShippingAddress? = null (if using a nested data class)
    // For simplicity, using a Map for now.
    val shippingAddress: Map<String, String> = emptyMap()
    // e.g. mapOf("fullName" to "John Doe", "street" to "123 Main St", "city" to "Anytown", "zip" to "12345", "country" to "USA")
) {
    // No-argument constructor for Firebase deserialization if needed,
    // but data classes with default values usually work.
    constructor() : this("", "", "", 0.0, "Pending", null, emptyMap())
}

// Optional: Define a ShippingAddress data class if it becomes complex
// data class ShippingAddress(
//     val fullName: String = "",
//     val street: String = "",
//     val city: String = "",
//     val state: String = "",
//     val zipCode: String = "",
//     val country: String = ""
// )
