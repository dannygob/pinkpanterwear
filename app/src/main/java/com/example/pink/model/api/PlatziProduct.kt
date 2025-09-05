package com.example.pink.model.api

data class PlatziProduct(
    val id: Int,
    val title: String?,
    val price: Double?,
    val description: String?,
    val images: List<String>?,
    val category: PlatziCategory?, // ✅ Renombrado para evitar conflicto
)

data class PlatziCategory(
    val id: Int,
    val name: String,
    val image: String?,
)