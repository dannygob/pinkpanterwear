package com.example.pink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.adapter.ProductAdapter
import com.example.pink.model.CartItem
import com.example.pink.network.FakeStoreApi
import com.example.pink.network.PlatziApi
import com.example.pink.repositories.ProductRepository
import com.example.pink.viewModel.CartViewModel
import com.example.pink.viewModel.ProductViewModel
import com.example.pink.viewModel.ProductViewModelFactory

class UserHomeFragment : Fragment() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewSuggested: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewSuggested = view.findViewById(R.id.recycler_view_suggested)
        progressBar = view.findViewById(R.id.progress_bar_home)

        // Inicializar ViewModels
        val repository = ProductRepository(PlatziApi.create(), FakeStoreApi.create())
        productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(repository)
        )[ProductViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        // Configurar adapter con productos y lógica de agregar al carrito
        productAdapter = ProductAdapter(mutableListOf()) { product ->
            val item = CartItem(
                productUniqueID = product.productUniqueID,
                productName = product.productName,
                productImage = product.productImage,
                productPrice = product.productPrice,
                quantity = 1
            )
            cartViewModel.addItem(item)
        }

        recyclerViewSuggested.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSuggested.adapter = productAdapter
        productAdapter.getSwipeHelper().attachToRecyclerView(recyclerViewSuggested)

        // Observar productos
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.updateList(products)
        }

        // Mostrar progreso
        productViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Cargar productos
        productViewModel.loadProducts()
    }
}