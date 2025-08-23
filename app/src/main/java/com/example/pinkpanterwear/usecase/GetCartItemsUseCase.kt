package com.example.pinkpanterwear.usecase


import com.example.pinkpanterwear.di.CartRepository
import com.example.pinkpanterwear.entities.CartItem
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: String): List<CartItem> {
        return cartRepository.getCartItems(userId)
    }
}
