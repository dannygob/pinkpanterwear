package com.example.pinkpanterwear.data

import com.example.pinkpanterwear.data.api.FakeStoreApiService
import com.example.pinkpanterwear.data.api.RetrofitInstance

// TODO: Consider using constructor injection for FakeStoreApiService for better testability.
class ProductRepository {

    private val apiService: FakeStoreApiService = RetrofitInstance.api

    suspend fun getAllProducts(): List<Product> {
        // TODO: Add proper error handling (e.g., try-catch, return Result<T>)
        return apiService.getAllProducts()
    }

    suspend fun getProductById(productId: Int): Product? { // Return nullable for not found or error
        // TODO: Add proper error handling
        return try {
            apiService.getProductDetails(productId)
        } catch (e: Exception) {
            // Log error e.g., Log.e("ProductRepository", "Error fetching product \${productId}", e)
            null // Or throw a custom domain exception
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        // TODO: Add proper error handling
        return apiService.getProductsByCategory(category)
    }

    suspend fun getAllCategories(): List<String> {
        // TODO: Add proper error handling
        return apiService.getAllCategories()
    }
}
