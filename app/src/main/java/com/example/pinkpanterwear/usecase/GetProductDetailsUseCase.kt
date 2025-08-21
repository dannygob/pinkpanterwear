package com.example.pinkpanterwear.usecase


import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.ProductRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Product? {
        return productRepository.getProductById(productId)
    }
}
