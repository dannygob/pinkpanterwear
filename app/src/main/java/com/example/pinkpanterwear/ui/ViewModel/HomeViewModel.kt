package com.example.pinkpanterwear.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.usecase.GetAllProductsUseCase
import com.example.pinkpanterwear.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            runCatching {
                getAllProductsUseCase()
            }.onSuccess { products ->
                _uiState.value = HomeUiState.Success(products)
            }.onFailure {
                _uiState.value =
                    HomeUiState.Error(
                        it.message ?: "Failed to fetch products"
                    )
            }
        }
    }
}
``
