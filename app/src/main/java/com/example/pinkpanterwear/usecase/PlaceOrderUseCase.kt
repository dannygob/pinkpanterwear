package com.example.pinkpanterwear.usecase

import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.entities.OrderItem
import com.example.pinkpanterwear.repositories.OrderRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order, items: List<OrderItem>): Result<String> {
        return try {
            val orderId = orderRepository.createOrder(order, items)
            if (orderId != null) {
                Result.success(orderId)
            } else {
                Result.failure(Exception("Failed to place order."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
