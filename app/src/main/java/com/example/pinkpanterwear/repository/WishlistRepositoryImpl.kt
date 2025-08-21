package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.entities.Wishlist
import com.example.pinkpanterwear.repositories.WishlistRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : WishlistRepository {

    // TODO: Get the actual current user ID. If using Firebase Auth:
    // private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    // Handle the case where the user is not logged in.

    // For now, using a placeholder:
    private val userId: String = "placeholder_user_id"


    // Reference to the user's wishlists collection
    private val userWishlistsCollection =
        firestore.collection("wishlists").document(userId).collection("userWishlists")


    override suspend fun createWishlist(name: String): Unit = withContext(Dispatchers.IO) {
        try {
            val wishlistData = hashMapOf(
                "name" to name,
                "productIds" to emptyList<String>() // Initially empty list of product IDs
            )
            userWishlistsCollection.add(wishlistData).await()
            Log.d("WishlistRepository", "Wishlist created successfully with name: $name")
        } catch (e: Exception) {
            Log.e("WishlistRepository", "Error creating wishlist", e)
        }
    }

    override fun getWishlists(): Flow<List<Wishlist>> = callbackFlow {
        val listenerRegistration: ListenerRegistration? = try {
            userWishlistsCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("WishlistRepository", "Error fetching wishlists", error)
                    close(error) // Close the flow with the error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val wishlists = snapshot.documents.mapNotNull { document ->
                        val name = document.getString("name") ?: ""
                        val productIds = document.get("productIds") as? List<String> ?: emptyList()
                        Wishlist(document.id, name, productIds)
                    }
                    trySendBlocking(wishlists)
                }
            }
        } catch (e: Exception) {
            Log.e("WishlistRepository", "Error setting up snapshot listener", e)
            close(e) // Close the flow with the error
            null
        }

        awaitClose {
            listenerRegistration?.remove()
        }
    }

    override suspend fun updateWishlistName(wishlistId: String, newName: String): Unit =
        withContext(Dispatchers.IO) {
            try {
                userWishlistsCollection.document(wishlistId).update("name", newName).await()
                Log.d(
                    "WishlistRepository",
                    "Wishlist name updated successfully for ID: $wishlistId"
                )
        } catch (e: Exception) {
                Log.e("WishlistRepository", "Error updating wishlist name", e)
        }
    }

    override suspend fun addProductToWishlist(wishlistId: String, productId: String): Unit =
        withContext(Dispatchers.IO) {
            try {
                userWishlistsCollection.document(wishlistId).update(
                    "productIds",
                    com.google.firebase.firestore.FieldValue.arrayUnion(productId)
                ).await()
                Log.d(
                    "WishlistRepository",
                    "Product added to wishlist successfully. Wishlist ID: $wishlistId, Product ID: $productId"
                )
        } catch (e: Exception) {
                Log.e("WishlistRepository", "Error adding product to wishlist", e)
        }
    }

    override suspend fun removeProductFromWishlist(wishlistId: String, productId: String): Unit =
        withContext(Dispatchers.IO) {
            try {
                userWishlistsCollection.document(wishlistId).update(
                    "productIds",
                    com.google.firebase.firestore.FieldValue.arrayRemove(productId)
                ).await()
                Log.d(
                    "WishlistRepository",
                    "Product removed from wishlist successfully. Wishlist ID: $wishlistId, Product ID: $productId"
                )
            } catch (e: Exception) {
                Log.e("WishlistRepository", "Error removing product from wishlist", e)
            }
        }

    override suspend fun deleteWishlist(wishlistId: String): Unit = withContext(Dispatchers.IO) {
        try {
            userWishlistsCollection.document(wishlistId).delete().await()
            Log.d("WishlistRepository", "Wishlist deleted successfully. Wishlist ID: $wishlistId")
        } catch (e: Exception) {
            Log.e("WishlistRepository", "Error deleting wishlist", e)
        }
    }

}
