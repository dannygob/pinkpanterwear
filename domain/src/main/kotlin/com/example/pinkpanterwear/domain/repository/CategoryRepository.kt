package com.example.pinkpanterwear.domain.repository

import com.example.pinkpanterwear.domain.entities.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategoryById(categoryId: String): Category?
    // Add more methods as needed
}
