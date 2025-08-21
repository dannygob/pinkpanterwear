package com.example.pinkpanterwear.di

import com.example.pinkpanterwear.entities.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: Int): Product?
    suspend fun getProductsByCategory(categoryName: String): List<Product>

    // Admin-Facing CRUD Operations
    suspend fun addProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product): Boolean
    suspend fun deleteProduct(productId: Int): Boolean
}
