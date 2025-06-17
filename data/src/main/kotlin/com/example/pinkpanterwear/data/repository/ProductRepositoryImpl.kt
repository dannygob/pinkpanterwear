package com.example.pinkpanterwear.data.repository

import com.example.pinkpanterwear.data.network.FakeStoreApiService
import com.example.pinkpanterwear.domain.entities.Product // Correct import for domain entity
import com.example.pinkpanterwear.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : ProductRepository {

    override suspend fun getAllProducts(): List<Product> {
        return try {
            apiService.getAllProducts()
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            emptyList() // Return empty list on error
        }
    }

    override suspend fun getProductById(productId: Int): Product? {
        return try {
            apiService.getProductDetails(productId)
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            null // Return null on error
        }
    }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> {
        return try {
            apiService.getProductsByCategory(categoryName)
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            emptyList() // Return empty list on error
        }
    }
}
