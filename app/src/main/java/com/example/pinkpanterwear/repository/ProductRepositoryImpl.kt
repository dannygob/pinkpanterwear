package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.network.ProductResponse
import com.example.pinkpanterwear.repositories.ProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private fun ProductResponse.toAppProduct(): Product {
    return Product(
        id = this.id,
        name = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        imageUrl = this.image,
        rating = Gson().toJson(this.rating) // Convert RatingResponse to JSON string
    )
}

class ProductRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : ProductRepository {

    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            apiService.getAllProducts().map { it.toAppProduct() }
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching all products", e)
            emptyList()
        }
    }

    override suspend fun getProductById(productId: Int): Product? = withContext(Dispatchers.IO) {
        try {
            apiService.getProductDetails(productId)?.toAppProduct()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching product by ID $productId", e)
            null
        }
    }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> =
        withContext(Dispatchers.IO) {
            try {
                apiService.getProductsByCategory(categoryName).map { it.toAppProduct() }
            } catch (e: Exception) {
                Log.e(
                    "ProductRepositoryImpl",
                    "Error fetching products for category '$categoryName'",
                    e
                )
                emptyList()
            }
        }

    override suspend fun getAllCategoriesFromFirestore(): List<String> =
        withContext(Dispatchers.IO) {
            try {
                apiService.getAllCategories()
        } catch (e: Exception) {
                Log.e("ProductRepositoryImpl", "Error fetching categories from API", e)
                emptyList()
        }
    }

    // Admin functions are not implemented for this API-based repository
    override suspend fun addProduct(product: Product): Boolean {
        Log.w("ProductRepositoryImpl", "addProduct is not supported for this API.")
        return false
    }

    override suspend fun updateProduct(product: Product): Boolean {
        Log.w("ProductRepositoryImpl", "updateProduct is not supported for this API.")
        return false
    }

    override suspend fun deleteProduct(productId: Int): Boolean {
        Log.w("ProductRepositoryImpl", "deleteProduct is not supported for this API.")
        return false
    }
}
