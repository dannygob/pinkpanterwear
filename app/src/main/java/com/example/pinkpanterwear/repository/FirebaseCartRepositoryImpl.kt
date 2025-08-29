package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.di.CartRepository
import com.example.pinkpanterwear.di.ProductRepository
import com.example.pinkpanterwear.entities.CartItem
import com.example.pinkpanterwear.entities.Product
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Data class to represent the structure in Firestore
data class FirestoreCartItem(
    val productId: Int = 0, // Default value for Firebase deserialization
    val quantity: Int = 0,
    val addedAt: Timestamp? = null, // Nullable for existing items without it
)

@Singleton
class FirebaseCartRepositoryImpl @Inject constructor(
    private val productRepository: ProductRepository, // Inject ProductRepository
) : CartRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth =
        FirebaseAuth.getInstance() // For getting current user if needed, though userId is passed
    private val usersCollection = firestore.collection("users")

    // Fetches cart items and their product details.
    override suspend fun getCartItems(userId: String): List<CartItem> =
        withContext(Dispatchers.IO) {
            if (userId.isEmpty()) {
                Log.w("CartRepository", "User ID is empty, cannot fetch cart items.")
                return@withContext emptyList()
            }
            val cartItemsList = mutableListOf<CartItem>()
            try {
                val snapshot =
                    usersCollection.document(userId).collection("cartItems").get().await()
                for (document in snapshot.documents) {
                    val firestoreCartItem = document.toObject(FirestoreCartItem::class.java)
                    if (firestoreCartItem != null) {
                        // Fetch full product details using ProductRepository
                        val product = productRepository.getProductById(firestoreCartItem.productId)
                        if (product != null) {
                            cartItemsList.add(
                                CartItem(
                                    userId = userId,
                                    productId = firestoreCartItem.productId,
                                    quantity = firestoreCartItem.quantity,
                                    size = null, // Assuming size is not stored in FirestoreCartItem for now
                                    productName = product.name,
                                    productPrice = product.price,
                                    productImageUrl = product.imageUrl
                                )
                            )
                        } else {
                            Log.w(
                                "CartRepository",
                                "Product with ID ${firestoreCartItem.productId} not found, but was in cart. Skipping."
                            )
                            // Optionally, could remove this orphaned cart item here.
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("CartRepository", "Error fetching cart items for user $userId", e)
                // Return empty list or throw custom exception
            }
            return@withContext cartItemsList
        }

    override suspend fun addCartItem(userId: String, product: Product, quantity: Int): Boolean =
        withContext(Dispatchers.IO) {
            if (userId.isEmpty()) {
                Log.w("CartRepository", "User ID is empty, cannot add item to cart.")
                return@withContext false
            }
            if (quantity <= 0) {
                Log.w("CartRepository", "Quantity must be positive to add item.")
                return@withContext false
            }

            val cartItemRef = usersCollection.document(userId).collection("cartItems")
                .document(product.id.toString())

            try {
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(cartItemRef)
                    if (snapshot.exists()) {
                        val existingCartItem = snapshot.toObject(FirestoreCartItem::class.java)
                        val currentQuantity = existingCartItem?.quantity ?: 0
                        val newQuantity = currentQuantity + quantity
                        transaction.update(cartItemRef, "quantity", newQuantity)
                    } else {
                        val newItem = FirestoreCartItem(
                            productId = product.id,
                            quantity = quantity,
                            addedAt = Timestamp.now()
                        )
                        transaction.set(cartItemRef, newItem)
                    }
                    null // Transaction must return null or a value
                }.await()
                return@withContext true
            } catch (e: Exception) {
                Log.e(
                    "CartRepository",
                    "Error adding/updating cart item for user ${userId}, product ${product.id}",
                    e
                )
                return@withContext false
            }
        }

    override suspend fun updateItemQuantity(
        userId: String,
        productId: Int,
        newQuantity: Int,
    ): Boolean = withContext(Dispatchers.IO) {
        if (userId.isEmpty()) return@withContext false
        val cartItemRef =
            usersCollection.document(userId).collection("cartItems").document(productId.toString())

        try {
            if (newQuantity <= 0) {
                cartItemRef.delete().await()
            } else {
                cartItemRef.update("quantity", newQuantity).await()
            }
            return@withContext true
        } catch (e: Exception) {
            Log.e(
                "CartRepository",
                "Error updating quantity for user ${userId}, product $productId",
                e
            )
            return@withContext false
        }
    }

    override suspend fun removeItem(userId: String, productId: Int): Boolean =
        withContext(Dispatchers.IO) {
            if (userId.isEmpty()) return@withContext false
            val cartItemRef = usersCollection.document(userId).collection("cartItems")
                .document(productId.toString())
            try {
                cartItemRef.delete().await()
                return@withContext true
            } catch (e: Exception) {
                Log.e(
                    "CartRepository",
                    "Error removing item for user ${userId}, product $productId",
                    e
                )
                return@withContext false
            }
        }

    override suspend fun clearCart(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (userId.isEmpty()) {
            Log.w("CartRepository", "User ID is empty, cannot clear cart.")
            return@withContext Result.failure(IllegalArgumentException("User ID cannot be empty"))
        }
        val cartItemsCollectionRef = usersCollection.document(userId).collection("cartItems")
        try {
            val snapshot = cartItemsCollectionRef.get().await()
            if (snapshot.isEmpty) {
                return@withContext Result.success(Unit) // Nothing to clear
            }
            val batch = firestore.batch()
            for (document in snapshot.documents) {
                batch.delete(document.reference)
            }
            batch.commit().await()
            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CartRepository", "Error clearing cart for user $userId", e)
            return@withContext Result.failure(e)
        }
    }
}
