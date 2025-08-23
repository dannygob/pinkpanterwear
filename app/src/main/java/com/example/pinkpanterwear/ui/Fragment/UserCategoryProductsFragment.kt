package com.example.pinkpanterwear.ui.Fragment

import UserProductDetailsActivity
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
import com.example.pinkpanterwear.ui.ViewModel.UserCategoryProductsViewModel
import com.example.pinkpanterwear.ui.adapters.ProductAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserCategoryProductsFragment : Fragment() {

    private val viewModel: UserCategoryProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString(ARG_CATEGORY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_category_products, container, false)
        recyclerView = view.findViewById(R.id.products_recycler_view) // Ensure this ID is correct
        progressBar = view.findViewById(R.id.category_products_progress_bar)
        errorTextView = view.findViewById(R.id.category_products_error_text_view)
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

        categoryName?.let {
            viewModel.fetchProductsForCategory(it)
        } ?: run {
            // Handle error: categoryName is null
            errorTextView.text = "Category not specified."
            errorTextView.isVisible = true
            progressBar.isVisible = false
            recyclerView.isVisible = false
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { products ->
                productAdapter.submitList(products)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                progressBar.isVisible = isLoading
                // Only show recyclerview if not loading AND no error from categoryName issue AND no error from VM
                recyclerView.isVisible =
                    !isLoading && categoryName != null && viewModel.error.value == null
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                if (categoryName != null) { // Only show VM error if category was valid
                    errorTextView.isVisible = error != null
                    errorTextView.text = error
                    recyclerView.isVisible = error == null && !viewModel.isLoading.value
                }
            }
        }
    }

    private fun navigateToProductDetails(product: com.example.pinkpanterwear.entities.Product) {
        val intent = Intent(activity, UserProductDetailsActivity::class.java).apply {
            putExtra(
                "PRODUCT_ID",
                product.id.toString()
            ) // API getProductDetails expects Int, Activity might still expect String
        }
        startActivity(intent)
    }

    companion object {
        private const val ARG_CATEGORY_NAME = "categoryName"

        @JvmStatic
        fun newInstance(categoryName: String) =
            UserCategoryProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_NAME, categoryName)
                }
            }
    }
}