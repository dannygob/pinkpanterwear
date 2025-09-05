package com.example.pink.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pink.model.Categories
import com.example.pink.model.Products
import com.example.pink.repositories.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    // 🛍️ Productos
    private val _products = MutableLiveData<List<Products>>()
    val products: LiveData<List<Products>> get() = _products

    // 📂 Categorías
    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> get() = _categories

    // ⏳ Estado de carga
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // 🧠 Copia de productos para restaurar después de filtrar
    private var allProducts: List<Products> = emptyList()

    // 🚀 Cargar productos
    fun loadProducts() {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.fetchAllProducts()
            allProducts = result
            _products.value = result
            _loading.value = false
        }
    }

    // 🚀 Cargar categorías
    fun loadCategories() {
        viewModelScope.launch {
            val result = repository.fetchCategories()
            _categories.value = result
        }
    }

    // 🔍 Filtrar productos por categoría
    fun filterProductsByCategory(categoryName: String) {
        val filtered = allProducts.filter {
            it.productCategory.equals(categoryName, ignoreCase = true)
        }
        _products.value = filtered
    }

    // 🔄 Restaurar todos los productos
    fun resetProductFilter() {
        _products.value = allProducts
    }
}