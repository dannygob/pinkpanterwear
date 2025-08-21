package com.example.pinkpanterwear.repository


import android.util.Log
import com.example.pinkpanterwear.di.ProductRepository
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.network.FakeStoreApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : ProductRepository {

    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getAllProducts()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching all products", e)
            emptyList() // Return empty list on error
        }
    }

    override suspend fun getProductById(productId: Int): Product? = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getProductDetails(productId)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching product by id", e)
            null // Return null on error
        }
    }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> =
        withContext(Dispatchers.IO) {
            return@withContext try {
            apiService.getProductsByCategory(categoryName)
        } catch (e: Exception) {
                Log.e("ProductRepositoryImpl", "Error fetching products by category", e)
            emptyList() // Return empty list on error
        }
    }

    override suspend fun addProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.addProduct(product)
            true
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error adding product", e)
            false
        }
    }

    override suspend fun updateProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.updateProduct(product.id, product)
            true
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error updating product", e)
            false
        }
    }

    override suspend fun deleteProduct(productId: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.deleteProduct(productId)
            true
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error deleting product", e)
            false
        }
    }
}

