package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.entities.OrderItem
import com.example.pinkpanterwear.repositories.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query // Correct import for Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreOrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : OrderRepository {

    private val ordersCollection = firestore.collection("orders")

    /**
     * Fetches all orders, sorted by orderDate descending.
     */
    override suspend fun getAllOrders(): List<Order> = withContext(Dispatchers.IO) {
        try {
            val snapshot = ordersCollection
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .await()
            // The @DocumentId annotation in Order.kt should handle mapping the ID.
            return@withContext snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
        } catch (e: Exception) {
            Log.e("FirestoreOrderRepositoryImpl", "Error fetching all orders", e)
            return@withContext emptyList()
        }
    }

    /**
     * Fetches all items for a specific order.
     */
    override suspend fun getOrderItems(orderId: String): List<OrderItem> =
        withContext(Dispatchers.IO) {
            if (orderId.isEmpty()) {
                Log.w("FirestoreOrderRepositoryImpl", "Order ID is empty, cannot fetch items.")
                return@withContext emptyList()
            }
            try {
                val snapshot = ordersCollection.document(orderId)
                    .collection("orderItems")
                    .get()
                    .await()
                return@withContext snapshot.documents.mapNotNull { it.toObject(OrderItem::class.java) }
            } catch (e: Exception) {
                Log.e(
                    "FirestoreOrderRepositoryImpl",
                    "Error fetching items for order ${orderId}",
                    e
                )
                return@withContext emptyList()
            }
        }

    // Example for fetching a single order by ID, could be useful later
    override suspend fun getOrderById(orderId: String): Order? = withContext(Dispatchers.IO) {
        if (orderId.isEmpty()) {
            Log.w("FirestoreOrderRepositoryImpl", "Order ID is empty.")
            return@withContext null
        }
        try {
            val documentSnapshot = ordersCollection.document(orderId).get().await()
            return@withContext documentSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreOrderRepositoryImpl", "Error fetching order by ID ${orderId}", e)
            return@withContext null
        }
    }

    /**
     * Creates a new order and its associated items in Firestore using a batch write.
     * @param order The Order object (orderId can be empty, will be generated).
     * @param items The list of OrderItem objects for this order.
     * @return The generated orderId if successful, null otherwise.
     */
    override suspend fun createOrder(order: Order, items: List<OrderItem>): String? =
        withContext(Dispatchers.IO) {
            try {
                val newOrderRef = ordersCollection.document() // Auto-generate ID for the order

                val orderDataToSave =
                    order.copy(orderId = newOrderRef.id) // Ensure the ID is part of the object saved

                val batch = firestore.batch()

                // 1. Set the main order document
                batch.set(newOrderRef, orderDataToSave)

                // 2. Add each OrderItem to the subcollection
                for (item in items) {
                    val itemRef =
                        newOrderRef.collection("orderItems").document(item.productId.toString())
                    batch.set(itemRef, item)
                }

                batch.commit().await()
                Log.d(
                    "FirestoreOrderRepositoryImpl",
                    "Order created successfully with ID: ${newOrderRef.id}"
                )
                return@withContext newOrderRef.id
            } catch (e: Exception) {
                Log.e("FirestoreOrderRepositoryImpl", "Error creating order", e)
                return@withContext null
            }
        }

    override suspend fun placeOrder(order: Order): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Assuming placeOrder might not always have items directly, or they are handled elsewhere.
            // For now, we'll call createOrder with an empty list of items.
            // In a real scenario, this might involve fetching cart items or other logic.
            val orderId = createOrder(order, emptyList())
            if (orderId != null) {
                Result.success(orderId)
            } else {
                Result.failure(Exception("Failed to place order: Order ID was null"))
            }
        } catch (e: Exception) {
            Log.e("FirestoreOrderRepositoryImpl", "Error placing order", e)
            Result.failure(e)
        }
    }
}
