package com.example.pinkpanterwear.data.repository

import com.example.pinkpanterwear.domain.entities.Order
import com.example.pinkpanterwear.domain.repository.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val ordersCollection = firestore.collection("orders")

    override suspend fun placeOrder(order: Order): Result<String> {
        return try {
            // Firestore will automatically generate an ID if order.orderId is empty
            // and @DocumentId is on a var String field.
            // Or, if orderId is pre-set, it will use that.
            // For auto-generated ID to be returned, we can do:
            val documentReference = ordersCollection.add(order).await()
            Result.success(documentReference.id)
        } catch (e: Exception) {
            // Log.e("OrderRepositoryImpl", "Error placing order", e)
            Result.failure(e)
        }
    }

    override suspend fun getOrderDetails(orderId: String): Order? {
        return try {
            val documentSnapshot = ordersCollection.document(orderId).get().await()
            documentSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            // Log.e("OrderRepositoryImpl", "Error getting order details for $orderId", e)
            null
        }
    }

    override suspend fun getUserOrders(userId: String): List<Order> {
        return try {
            val querySnapshot = ordersCollection.whereEqualTo("userId", userId).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(Order::class.java) }
        } catch (e: Exception) {
            // Log.e("OrderRepositoryImpl", "Error getting orders for user $userId", e)
            emptyList()
        }
    }
}
