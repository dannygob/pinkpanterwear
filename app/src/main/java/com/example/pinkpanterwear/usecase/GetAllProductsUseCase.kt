package com.example.pinkpanterwear.usecase

import com.example.pinkpanterwear.domain.entities.Product
import com.example.pinkpanterwear.domain.repository.ProductRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getAllProducts()
    }
}
