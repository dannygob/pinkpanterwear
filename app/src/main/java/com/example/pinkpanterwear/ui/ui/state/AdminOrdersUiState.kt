
sealed interface AdminOrdersUiState {
    object Loading : AdminOrdersUiState
    data class Success(val orders: List<Order>) : AdminOrdersUiState
    data class Error(val message: String) : AdminOrdersUiState
}
