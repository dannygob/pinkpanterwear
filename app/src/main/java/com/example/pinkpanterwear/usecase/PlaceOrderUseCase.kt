package com.example.pinkpanterwear.usecase

import com.example.pinkpanterwear.domain.entities.Order
import com.example.pinkpanterwear.domain.repository.OrderRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: Order): Result<String> {
        // Add any business logic here before placing the order if needed
        return orderRepository.placeOrder(order)
    }
}
