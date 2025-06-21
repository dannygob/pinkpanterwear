package com.example.pinkpanterwear.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.entities.Category
import com.example.pinkpanterwear.usecase.GetAllCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel // Import for Hilt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // Import for Hilt

@HiltViewModel
class UserCategoryViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Assuming use case returns List<Category> directly or Result<List<Category>>
                // For simplicity, let's assume it returns List<Category> and throws on error
                _categories.value = getAllCategoriesUseCase()
            } catch (e: Exception) {
                _error.value = "Failed to fetch categories: ${e.message}"
                // Consider more specific error handling or logging
            } finally {
                _isLoading.value = false
            }
        }
    }
}
