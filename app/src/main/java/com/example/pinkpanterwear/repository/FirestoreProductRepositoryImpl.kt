package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.di.ProductRepository
import com.example.pinkpanterwear.entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ProductRepository {

    private val productsCollection = firestore.collection("products")

    override suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsCollection.get().await()
            return@withContext snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            Log.e("FirestoreProductRepositoryImpl", "Error fetching all products from Firestore", e)
            return@withContext emptyList()
        }
    }

    override suspend fun getProductById(productId: Int): Product? =
        withContext(Dispatchers.IO) {
            try {
                val documentSnapshot =
                    productsCollection.document(productId.toString()).get().await()
                return@withContext documentSnapshot.toObject(Product::class.java)
            } catch (e: Exception) {
                Log.e(
                    "FirestoreProductRepositoryImpl",
                    "Error fetching product $productId from Firestore",
                    e
                )
                return@withContext null
            }
        }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot =
                    productsCollection.whereEqualTo("category", categoryName).get().await()
                return@withContext snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
            } catch (e: Exception) {
                Log.e(
                    "FirestoreProductRepositoryImpl",
                    "Error fetching products for category '${categoryName}' from Firestore",
                    e
                )
                return@withContext emptyList()
            }
        }

    // Admin-Facing CRUD Operations
    override suspend fun addProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        if (product.id == 0) {
            Log.e("FirestoreProductRepositoryImpl", "Product ID must be set before adding.")
            return@withContext false
        }
        try {
            productsCollection.document(product.id.toString()).set(product).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e(
                "FirestoreProductRepositoryImpl",
                "Error adding product ${product.id} to Firestore",
                e
            )
            return@withContext false
        }
    }

    override suspend fun updateProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        if (product.id == 0) {
            Log.e("FirestoreProductRepositoryImpl", "Product ID must be valid for updating.")
            return@withContext false
        }
        try {
            productsCollection.document(product.id.toString()).set(product, SetOptions.merge())
                .await()
            return@withContext true
        } catch (e: Exception) {
            Log.e(
                "FirestoreProductRepositoryImpl",
                "Error updating product ${product.id} in Firestore",
                e
            )
            return@withContext false
        }
    }

    override suspend fun deleteProduct(productId: Int): Boolean = withContext(Dispatchers.IO) {
        if (productId == 0) {
            Log.e("FirestoreProductRepositoryImpl", "Invalid Product ID for deletion.")
            return@withContext false
        }
        try {
            productsCollection.document(productId.toString()).delete().await()
            return@withContext true
        } catch (e: Exception) {
            Log.e(
                "FirestoreProductRepositoryImpl",
                "Error deleting product $productId from Firestore",
                e
            )
            return@withContext false
        }
    }
}
