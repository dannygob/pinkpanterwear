package com.example.pink.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Products(
    @PrimaryKey val productUniqueID: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productImage: String = "",
    val productCategory: String = "",
    val productStatus: String = "",
    val dateCreated: String = "",
    val timeCreated: String = "",
    val productPrice: Double = 0.0,
    val productDeleted: String,  // ← Mejora: precio decimal para más flexibilidad
)