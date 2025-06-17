package com.example.pinkpanterwear.domain.repository

import com.example.pinkpanterwear.domain.entities.Order
import com.example.pinkpanterwear.domain.entities.OrderItem // If needed for order details

interface OrderRepository {
    suspend fun placeOrder(order: Order): Result<String> // Returns Order ID or error
    suspend fun getOrderDetails(orderId: String): Order?
    suspend fun getUserOrders(userId: String): List<Order>
    // Add more methods for order history, status updates, etc.
}
