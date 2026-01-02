package com.example.pinkpanterwear.repositories

import com.example.pinkpanterwear.category.Category

interface CategoryRepository {
    suspend fun getAllCategories(): List<Category>
    suspend fun getCategoryById(categoryId: String): Category?
    // Add more methods as needed
}
