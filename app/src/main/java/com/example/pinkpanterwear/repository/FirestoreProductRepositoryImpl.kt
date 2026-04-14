package com.example.pinkpanterwear.repository

import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    private val productsCollection = firestore.collection("products")

    override suspend fun getAllProducts(): List<Product> =
        withContext(Dispatchers.IO) {

            val snapshot = productsCollection.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Product::class.java)
            }
        }

    override suspend fun getProductById(productId: Int): Product? =
        withContext(Dispatchers.IO) {

            require(productId > 0) { "Product ID must be greater than 0" }

            productsCollection
                .document(productId.toString())
                .get()
                .await()
                .toObject(Product::class.java)
        }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> =
        withContext(Dispatchers.IO) {

            require(categoryName.isNotBlank()) { "Category cannot be empty" }

            val snapshot =
                productsCollection
                    .whereEqualTo("category", categoryName)
                    .get()
                    .await()

            snapshot.documents.mapNotNull {
                it.toObject(Product::class.java)
            }
        }

    override suspend fun getAllCategoriesFromFirestore(): List<String> =
        withContext(Dispatchers.IO) {

            val snapshot = productsCollection.get().await()

            snapshot.documents
                .mapNotNull { it.getString("category") }
                .distinct()
        }

    // =========================
    // Admin CRUD
    // =========================

    override suspend fun addProduct(product: Product) =
        withContext(Dispatchers.IO) {

            require(product.id > 0) { "Product ID must be set before adding" }

            productsCollection
                .document(product.id.toString())
                .set(product)
                .await()
        }

    override suspend fun updateProduct(product: Product) =
        withContext(Dispatchers.IO) {

            require(product.id > 0) { "Product ID must be valid for update" }

            productsCollection
                .document(product.id.toString())
                .set(product, SetOptions.merge())
                .await()
        }

    override suspend fun deleteProduct(productId: Int) =
        withContext(Dispatchers.IO) {

            require(productId > 0) { "Invalid product ID" }

            productsCollection
                .document(productId.toString())
                .delete()
                .await()
        }
}
``
