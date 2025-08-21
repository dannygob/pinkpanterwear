package com.example.pinkpanterwear.di

import com.example.pinkpanterwear.entities.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: Int): Product?
    suspend fun getProductsByCategory(categoryName: String): List<Product>
}
