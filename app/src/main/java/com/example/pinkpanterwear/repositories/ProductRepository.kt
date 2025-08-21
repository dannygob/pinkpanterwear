package com.example.pinkpanterwear.repositories

import com.example.pinkpanterwear.entities.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: Int): Product?
    suspend fun getProductsByCategory(categoryName: String): List<Product>
    suspend fun addProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product): Boolean
    suspend fun deleteProduct(productId: Int): Boolean
}
