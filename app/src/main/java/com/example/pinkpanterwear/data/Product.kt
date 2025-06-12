package com.example.pinkpanterwear.data

// Note: @DocumentId could be used for a String field if Firestore auto-generated IDs were primary.
// Here, 'id' (Int) is the business key, and Firestore document ID will be id.toString().

data class Product(
    val id: Int = 0, // Will be used as the key for the Firestore document (converted to String)
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "", // Admin will provide a URL
    val category: String = "", // Admin will input category name as a string
    val rating: Rating? = null // Nullable, as new products won't have ratings
    // Consider adding stockQuantity: Int = 0 if managing inventory
) {
    // No-argument constructor for Firebase deserialization.
    // Kotlin data classes with default values for all properties generate this.
}

data class Rating(
    val rate: Double = 0.0,
    val count: Int = 0
) {
    // No-argument constructor for Firebase
    constructor() : this(0.0, 0)
}
