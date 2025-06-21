package com.example.pinkpanterwear.usecase

import com.example.pinkpanterwear.domain.repository.CartRepository
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: String): List<CartItem> {
        return cartRepository.getCartItems(userId)
    }
}
