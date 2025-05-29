package com.example.pinkpanterwear.data

import com.example.pinkpanterwear.data.Product // Assuming Product data class is in this package
import com.example.pinkpanterwear.data.api.FakeStoreApiService


class ProductRepository {

    // TODO: Inject FakeStoreApiService here using Dependency Injection (e.g., Hilt)
    private val apiService: FakeStoreApiService = TODO("Inject FakeStoreApiService")

    suspend fun getProductDetails(productId: String): Product? {
        return try {
            apiService.getProductDetails(productId)
        } catch (e: Exception) {
            // TODO: Handle network errors or other exceptions (logging, etc.)
            e.printStackTrace()
            null
        }
    }

    suspend fun getCategories(): List<String> {
        return try {
            apiService.getCategories()
        } catch (e: Exception) {
            // TODO: Handle network errors or other exceptions
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProductsByCategory(categoryName: String): List<Product> {
        return apiService.getProductsByCategory(categoryName)
    }
}