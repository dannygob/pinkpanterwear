package com.example.pinkpanterwear.usecase


import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.ProductRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return productRepository.getAllProducts()
    }
}
