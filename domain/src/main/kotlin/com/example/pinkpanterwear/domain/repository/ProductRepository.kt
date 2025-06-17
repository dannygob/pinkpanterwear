package com.example.pinkpanterwear.domain.repository

import com.example.pinkpanterwear.domain.entities.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: Int): Product?
    suspend fun getProductsByCategory(categoryName: String): List<Product>
    // Add more methods as needed, e.g., for searching or admin operations
}
