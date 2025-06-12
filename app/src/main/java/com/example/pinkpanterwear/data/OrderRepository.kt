package com.example.pinkpanterwear.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject // For DocumentSnapshot.toObject<T>()
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("orders")

    /**
     * Fetches all orders, sorted by orderDate descending.
     */
    suspend fun getAllOrders(): List<Order> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ordersCollection
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .await()
            // The @DocumentId annotation in Order.kt should handle mapping the ID.
            return@withContext snapshot.documents.mapNotNull { it.toObject<Order>() }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching all orders", e)
            return@withContext emptyList()
        }
    }

    /**
     * Fetches all items for a specific order.
     */
    suspend fun getOrderItems(orderId: String): List<OrderItem> = withContext(Dispatchers.IO) {
        if (orderId.isEmpty()) {
            Log.w("OrderRepository", "Order ID is empty, cannot fetch items.")
            return@withContext emptyList()
        }
        try {
            val snapshot = ordersCollection.document(orderId)
                .collection("orderItems")
                .get()
                .await()
            // If OrderItem.kt has @DocumentId for its own ID (e.g. from product ID string)
            // then it.toObject<OrderItem>() would map it.
            // Otherwise, if document ID is productId, we might need to set it manually if not in fields.
            // Current OrderItem.kt does not use @DocumentId, assumes fields are sufficient.
            return@withContext snapshot.documents.mapNotNull { it.toObject<OrderItem>() }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching items for order ${orderId}", e)
            return@withContext emptyList()
        }
    }

    // Example for fetching a single order by ID, could be useful later
    suspend fun getOrderById(orderId: String): Order? = withContext(Dispatchers.IO) {
        if (orderId.isEmpty()) {
            Log.w("OrderRepository", "Order ID is empty.")
            return@withContext null
        }
        try {
            val documentSnapshot = ordersCollection.document(orderId).get().await()
            return@withContext documentSnapshot.toObject<Order>()
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching order by ID ${orderId}", e)
            return@withContext null
        }
    }
}
