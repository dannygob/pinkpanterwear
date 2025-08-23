package com.example.pinkpanterwear.repository

import android.util.Log
import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.entities.OrderItem
import com.example.pinkpanterwear.repositories.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val ordersCollection = firestore.collection("orders")
    private val orderItemsCollection = firestore.collection("order_items")

    override suspend fun getAllOrders(): List<Order> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = ordersCollection.get().await()
            querySnapshot.documents.mapNotNull { it.toObject(Order::class.java) }
        } catch (e: Exception) {
            Log.e("OrderRepositoryImpl", "Error fetching all orders", e)
            emptyList()
        }
    }

    override suspend fun getOrderItems(orderId: String): List<OrderItem> =
        withContext(Dispatchers.IO) {
            try {
                val querySnapshot = orderItemsCollection
                    .whereEqualTo("orderId", orderId)
                    .get().await()
                querySnapshot.documents.mapNotNull { it.toObject(OrderItem::class.java) }
        } catch (e: Exception) {
                Log.e("OrderRepositoryImpl", "Error fetching order items for orderId: $orderId", e)
                emptyList()
            }
        }

    override suspend fun getOrderById(orderId: String): Order? = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = ordersCollection.document(orderId).get().await()
            documentSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            Log.e("OrderRepositoryImpl", "Error fetching order by id: $orderId", e)
            null
        }
    }

    override suspend fun createOrder(order: Order, items: List<OrderItem>): String? =
        withContext(Dispatchers.IO) {
            try {
                val orderDocument = ordersCollection.document()
                val batch = firestore.batch()

                batch.set(orderDocument, order)

                items.forEach { item ->
                    val itemDocument = orderItemsCollection.document()
                    batch.set(itemDocument, item)
                }

                batch.commit().await()
                orderDocument.id
            } catch (e: Exception) {
                Log.e("OrderRepositoryImpl", "Error creating order", e)
                null
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
            Log.e("OrderRepositoryImpl", "Error placing order", e)
            Result.failure(e)
        }
    }
}
