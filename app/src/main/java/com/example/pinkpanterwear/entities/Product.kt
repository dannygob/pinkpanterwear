package com.example.pinkpanterwear.entities

data class Product(
    val id: Int = 0, // Will be used as the key for the Firestore document (converted to String)
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "", // Admin will provide a URL
    val category: String = "", // Admin will input category name as a string
    val rating: Rating? = null, // Nullable, as new products won't have ratings
    val stockQuantity: Int = 0, // Consider adding stockQuantity: Int = 0 if managing inventory
)
data class Rating(
    val rate: Double = 0.0,
    val count: Int = 0
) {
    // No-argument constructor for Firebase
    constructor() : this(0.0, 0)
}
