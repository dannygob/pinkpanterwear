package com.example.pinkpanterwear.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pinkpanterwear.AuthHelper
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.repositories.CartRepository
import com.example.pinkpanterwear.repositories.ProductRepository
import com.example.pinkpanterwear.ui.event.ProductDetailsEvent
import com.example.pinkpanterwear.ui.state.ProductDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val authHelper: AuthHelper
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProductDetailsEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProductDetailsEvent> = _events.asSharedFlow()

    // Si en el futuro las tallas vienen del backend, esto lo puedes cargar dinámicamente.
    private val defaultSizes = listOf("S", "M", "L", "XL")

    fun loadProductById(productIdString: String) {
        val productId = productIdString.toIntOrNull()
        if (productId == null) {
            _uiState.value = ProductDetailsUiState.Error("Formato de ID de producto inválido.")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading

            runCatching {
                productRepository.getProductById(productId)
            }.onSuccess { product ->
                _uiState.value =
                    if (product != null) {
                        ProductDetailsUiState.Content(
                            product = product,
                            availableSizes = defaultSizes
                        )
                    } else {
                        ProductDetailsUiState.NotFound
                    }
            }.onFailure { t ->
                Log.e("ProductDetailsVM", "Error loading product $productId", t)
                _uiState.value =
                    ProductDetailsUiState.Error(t.message ?: "Error cargando el producto.")
            }
        }
    }

    fun addToCart(
        product: Product,
        selectedSize: String?,
        quantity: Int
    ) {
        if (quantity <= 0) {
            _events.tryEmit(ProductDetailsEvent.Message("La cantidad debe ser mayor que 0."))
            return
        }

        // Validación “suave” de talla (opcional)
        if (selectedSize != null && selectedSize !in defaultSizes) {
            _events.tryEmit(ProductDetailsEvent.Message("Talla seleccionada no válida."))
            return
        }

        val userId = authHelper.getCurrentUser()?.uid
        if (userId.isNullOrEmpty()) {
            _uiState.value = ProductDetailsUiState.LoggedOut
            _events.tryEmit(ProductDetailsEvent.Message("Inicia sesión para añadir al carrito."))
            return
        }

        viewModelScope.launch {
            runCatching {
                // TODO: si quieres guardar talla, tendrás que modificar CartItem/FirestoreCartItem y repo.
                cartRepository.addCartItem(userId, product, quantity)
            }.onSuccess { success ->
                if (success) {
                    Log.i("ProductDetailsVM", "${product.name} añadido/actualizado en carrito.")
                    _events.emit(ProductDetailsEvent.Message("Añadido al carrito ✅"))
                } else {
                    _events.emit(ProductDetailsEvent.Message("No se pudo añadir al carrito."))
                }
            }.onFailure { t ->
                Log.e("ProductDetailsVM", "Error adding to cart", t)
                _events.emit(ProductDetailsEvent.Message(t.message ?: "Error añadiendo al carrito."))
            }
        }
    }
}
``
