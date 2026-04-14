package com.example.pinkpanterwear.ui.state

import com.example.pinkpanterwear.category.Category

sealed interface UserCategoryUiState {

    object Loading : UserCategoryUiState

    data class Success(
        val categories: List<Category>
    ) : UserCategoryUiState

    data class Error(
        val message: String
    ) : UserCategoryUiState
}
``
