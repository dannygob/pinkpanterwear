package com.example.pink.model

data class UnifiedProduct(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: String,
    val price: Double,
    val source: String, // Ej: "Platzi" o "FakeStore"
)