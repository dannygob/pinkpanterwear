package com.example.pinkpanterwear.repository


import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.repositories.ProductRepository
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
