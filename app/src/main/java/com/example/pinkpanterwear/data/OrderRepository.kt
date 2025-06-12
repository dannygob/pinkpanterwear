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

    /**
     * Creates a new order and its associated items in Firestore using a batch write.
     * @param order The Order object (orderId can be empty, will be generated).
     * @param items The list of OrderItem objects for this order.
     * @return The generated orderId if successful, null otherwise.
     */
    suspend fun createOrder(order: Order, items: List<OrderItem>): String? = withContext(Dispatchers.IO) {
        try {
            val newOrderRef = ordersCollection.document() // Auto-generate ID for the order

            // The @DocumentId field in Order.kt (order.orderId) will be populated by Firestore
            // when toObject is called. For saving, we ensure the object passed to set()
            // doesn't necessarily need the ID if Firestore is generating it.
            // However, if we want to use this ID immediately, or if Order.kt expects it,
            // it's good to assign it. Let's assume Order.kt has @DocumentId var orderId.
            // The 'order' object passed in might have an empty orderId.
            // We will use the newOrderRef.id. The passed 'order' object is more like a template.

            val orderDataToSave = order.copy(orderId = newOrderRef.id) // Ensure the ID is part of the object saved

            val batch = firestore.batch()

            // 1. Set the main order document
            batch.set(newOrderRef, orderDataToSave)

            // 2. Add each OrderItem to the subcollection
            for (item in items) {
                // Use product ID as the document ID for order items for easy lookup/overwrite if needed
                val itemRef = newOrderRef.collection("orderItems").document(item.productId.toString())
                batch.set(itemRef, item)
            }

            batch.commit().await()
            Log.d("OrderRepository", "Order created successfully with ID: ${newOrderRef.id}")
            return@withContext newOrderRef.id
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error creating order", e)
            return@withContext null
        }
    }
}
