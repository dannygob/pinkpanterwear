package com.example.pinkpanterwear.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.usecase.GetAllCategoriesUseCase
import com.example.pinkpanterwear.ui.state.UserCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCategoryViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UserCategoryUiState>(UserCategoryUiState.Loading)
    val uiState: StateFlow<UserCategoryUiState> = _uiState.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _uiState.value = UserCategoryUiState.Loading

            runCatching {
                getAllCategoriesUseCase()
            }.onSuccess { categories ->
                _uiState.value =
                    UserCategoryUiState.Success(categories)
            }.onFailure {
                _uiState.value =
                    UserCategoryUiState.Error(
                        it.message ?: "Error al cargar categorías"
                    )
            }
        }
    }
}
``
