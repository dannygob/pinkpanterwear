package com.example.pinkpanterwear.repositories

import android.util.Log
import com.example.pinkpanterwear.entities.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class ProductRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val productsCollection = firestore.collection("products")

    // --- User-Facing Read Operations ---

    suspend fun getAllProductsFromFirestore(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsCollection.get().await()
            // Note: Product.id is Int. Firestore doc ID is product.id.toString().
            // When deserializing with toObject<Product>(), if Product has an @DocumentId String field, it would be populated.
            // Since we don't, we'll rely on the 'id' field within the document data itself.
            return@withContext snapshot.documents.mapNotNull { it.toObject<Product>() }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching all products from Firestore", e)
            return@withContext emptyList()
        }
    }

    suspend fun getProductByIdFromFirestore(productId: Int): Product? =
        withContext(Dispatchers.IO) {
            try {
                val documentSnapshot =
                    productsCollection.document(productId.toString()).get().await()
                return@withContext documentSnapshot.toObject<Product>()
            } catch (e: Exception) {
                Log.e("ProductRepository", "Error fetching product ${productId} from Firestore", e)
                return@withContext null
            }
        }

    suspend fun getProductsByCategoryFromFirestore(categoryName: String): List<Product> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot =
                    productsCollection.whereEqualTo("category", categoryName).get().await()
                return@withContext snapshot.documents.mapNotNull { it.toObject<Product>() }
            } catch (e: Exception) {
                Log.e(
                    "ProductRepository",
                    "Error fetching products for category '${categoryName}' from Firestore",
                    e
                )
                return@withContext emptyList()
            }
        }

    suspend fun getAllCategoriesFromFirestore(): List<String> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsCollection.get().await()
            val products = snapshot.documents.mapNotNull { it.toObject<Product>() }
            return@withContext products.map { it.category }.distinct().filter { it.isNotBlank() }
                .sorted()
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching categories from Firestore products", e)
            return@withContext emptyList()
        }
    }

    // --- Admin-Facing CRUD Operations ---

    suspend fun addProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        // Ensure product.id is set (e.g., from a counter or UUID if not user-defined before calling)
        // For now, assume product.id is provided and unique for simplicity in this step.
        // A real app might auto-generate ID if not provided, or check for conflicts.
        if (product.id == 0) { // Basic check, 0 might be a valid ID in some systems but often not.
            Log.e("ProductRepository", "Product ID must be set before adding.")
            // In a real scenario, you might generate an ID here if not using Firestore auto-generated doc IDs
            // For example, by querying the last ID or using a separate counter document.
            // Or, let Firestore generate the document ID and store that ID in the product object.
            // For this plan, we use product.id as the business key and product.id.toString() as document ID.
            return@withContext false
        }
        try {
            productsCollection.document(product.id.toString()).set(product).await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error adding product ${product.id} to Firestore", e)
            return@withContext false
        }
    }

    suspend fun updateProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        if (product.id == 0) {
            Log.e("ProductRepository", "Product ID must be valid for updating.")
            return@withContext false
        }
        try {
            // Using set with SetOptions.merge() to only update fields present in the product object,
            // or .update() with specific fields if preferred. SetOptions.merge() is safer for partial updates.
            // However, if 'product' contains all fields, .set() is fine and acts as overwrite.
            productsCollection.document(product.id.toString()).set(product, SetOptions.merge())
                .await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error updating product ${product.id} in Firestore", e)
            return@withContext false
        }
    }

    suspend fun deleteProduct(productId: Int): Boolean = withContext(Dispatchers.IO) {
        if (productId == 0) {
            Log.e("ProductRepository", "Invalid Product ID for deletion.")
            return@withContext false
        }
        try {
            productsCollection.document(productId.toString()).delete().await()
            return@withContext true
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error deleting product ${productId} from Firestore", e)
            return@withContext false
        }
    }
}