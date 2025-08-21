package com.example.pinkpanterwear.repositories

import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.entities.OrderItem

interface OrderRepository {
    suspend fun getAllOrders(): List<Order>
    suspend fun getOrderItems(orderId: String): List<OrderItem>
    suspend fun getOrderById(orderId: String): Order?
    suspend fun createOrder(order: Order, items: List<OrderItem>): String?
    suspend fun placeOrder(order: Order): Result<String>
}
