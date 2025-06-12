package com.example.pinkpanterwear.data

import com.google.firebase.firestore.DocumentId

data class OrderItem(
    // If productId is the document ID in the subcollection:
    // @DocumentId var productIdString: String = "", // Firestore doc ID is string
    // val productId: Int = 0, // Then also store the actual Int product ID

    // Simpler: Assume document ID is product.id.toString(), and we store productId as Int.
    // No @DocumentId needed here if we construct it from snapshot.id or pass it.
    val productId: Int = 0, // The actual Product ID (Int)
    val productName: String = "", // Denormalized for display
    val quantity: Int = 0,
    val pricePerItem: Double = 0.0 // Price of one unit at the time of order
) {
    // No-argument constructor for Firebase
    constructor() : this(0, "", 0, 0.0)
}
