package com.example.pinkpanterwear.entities

import androidx.room.Embedded

data class WishlistItemWithProduct(
    @Embedded
    val product: Product,
    val dateAdded: Long
)
