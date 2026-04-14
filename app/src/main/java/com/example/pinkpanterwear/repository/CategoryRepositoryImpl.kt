package com.example.pinkpanterwear.repository

import com.example.pinkpanterwear.category.Category
import com.example.pinkpanterwear.network.FakeStoreApiService
import com.example.pinkpanterwear.repositories.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: FakeStoreApiService
) : CategoryRepository {

    override suspend fun getAllCategories(): List<Category> {
        val categoryNames = apiService.getAllCategories()

        return categoryNames.map { name ->
            Category(
                id = name,
                name = name,
                image = "" // La API no proporciona imagen
            )
        }
    }

    override suspend fun getCategoryById(categoryId: String): Category? {
        // La API no soporta getById, así que reutilizamos la lista
        return getAllCategories()
            .firstOrNull { it.id.equals(categoryId, ignoreCase = true) }
    }
}
