package com.example.pinkpanterwear.repository


import com.example.pinkpanterwear.domain.entities.Category
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> {
        return try {
            val categories = apiService.getAllCategories()
            categories
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            emptyList() // Return empty list on error
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        return try {
            val categories = apiService.getAllCategories()
            return categories.find { it.id == categoryId }
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            return null // Return null on error
        }
    }
}
