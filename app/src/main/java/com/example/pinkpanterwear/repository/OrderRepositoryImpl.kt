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

    override suspend fun createOrder(order: Order, items: List<OrderItem>): String? =
        withContext(Dispatchers.IO) {
            try {
                val orderDocument = ordersCollection.document()
                order.orderId = orderDocument.id // Set the orderId in the Order object

                val batch = firestore.batch()
                batch.set(orderDocument, order)

                for (item in items) {
                    val itemDocument = orderItemsCollection.document()
                    val itemWithOrderId = item.copy(orderId = order.orderId)
                    batch.set(itemDocument, itemWithOrderId)
                }

                batch.commit().await()
                order.orderId
            } catch (e: Exception) {
                Log.e("OrderRepositoryImpl", "Error creating order", e)
                null
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
}
