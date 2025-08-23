package com.example.pinkpanterwear.usecase


import com.example.pinkpanterwear.domain.entities.Category
import com.example.pinkpanterwear.repositories.CategoryRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getAllCategories()
    }
}
