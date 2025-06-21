package com.example.pinkpanterwear.repository


import com.example.pinkpanterwear.entities.Category
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> {
        return try {
            val categoryNames = apiService.getAllCategories()
            // Map category names to Category objects. Using name as ID.
            categoryNames.map { name -> Category(id = name, name = name) }
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            emptyList() // Return empty list on error
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        return try {
            val categoryNames = apiService.getAllCategories()
            if (categoryNames.contains(categoryId)) {
                Category(id = categoryId, name = categoryId)
            } else {
                null
            }
        } catch (e: Exception) {
            // Log error or handle specific exceptions
            null // Return null on error
        }
    }
}
