package com.example.pinkpanterwear.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.UserProductDetailsActivity // Assuming this is the correct details activity
import com.example.pinkpanterwear.domain.entities.Product // Changed import for Product
import com.example.pinkpanterwear.presentation.features.home.HomeViewModel // Added new import for HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels() // Type will now resolve to new import
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.home_products_recycler_view)
        progressBar = view.findViewById(R.id.home_progress_bar)
        errorTextView = view.findViewById(R.id.home_error_text_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductAdapter { product ->
            navigateToProductDetails(product)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.products.collectLatest { products ->
                productAdapter.submitList(products)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.isLoading.collectLatest { isLoading ->
                progressBar.isVisible = isLoading
                recyclerView.isVisible = !isLoading && homeViewModel.error.value == null
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.error.collectLatest { error ->
                errorTextView.isVisible = error != null
                errorTextView.text = error
                recyclerView.isVisible = error == null && !homeViewModel.isLoading.value
            }
        }
    }

    private fun navigateToProductDetails(product: Product) {
        val intent = Intent(activity, UserProductDetailsActivity::class.java).apply {
            // Assuming UserProductDetailsActivity expects PRODUCT_ID as Int after Product.id change
            putExtra("PRODUCT_ID", product.id.toString()) // API getProductDetails expects Int, but Activity might still expect String from intent
        }
        startActivity(intent)
    }
}
