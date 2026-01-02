package com.example.pinkpanterwear.repository

import com.example.pinkpanterwear.category.Category
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> {
        return try {
            val categoryNames = apiService.getAllCategories()
            categoryNames.map { name ->
                Category(id = name, name = name, image = "") // No image from this API endpoint
            }
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            emptyList() // Return empty list on error
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        return try {
            // The API doesn't have a direct "get by ID" for categories,
            // so we fetch all and find the one we need.
            val categoryNames = apiService.getAllCategories()
            val foundName = categoryNames.find { it.equals(categoryId, ignoreCase = true) }
            foundName?.let { Category(id = it, name = it, image = "") }
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            null // Return null on error
        }
    }
}
