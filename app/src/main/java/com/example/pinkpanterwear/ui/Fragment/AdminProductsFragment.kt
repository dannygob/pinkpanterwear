package com.example.pinkpanterwear.ui.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.entities.Product
import com.example.pinkpanterwear.ui.ViewModel.AdminProductsViewModel
import com.example.pinkpanterwear.ui.activities.AdminProductAddEditActivity
import com.example.pinkpanterwear.ui.adapters.AdminProductAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminProductsFragment : Fragment() {

    private val viewModel: AdminProductsViewModel by viewModels()
    private lateinit var productAdapter: AdminProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView
    private lateinit var addProductFab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_products, container, false)
        recyclerView = view.findViewById(R.id.admin_products_recycler_view)
        progressBar = view.findViewById(R.id.admin_products_progress_bar)
        messageTextView = view.findViewById(R.id.admin_products_message_text_view)
        addProductFab = view.findViewById(R.id.admin_add_product_fab)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        // Refresh product list when fragment becomes visible, e.g. after adding/editing a product
        viewModel.fetchAdminProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = AdminProductAdapter(
            onEditClicked = { product ->
                // Navigate to AdminProductAddEditActivity with product ID
                Log.d("AdminProductsFrag", "Edit product: ${product.id}")
                val intent = Intent(activity, AdminProductAddEditActivity::class.java)
                intent.putExtra("PRODUCT_ID_INT", product.id) // Pass Int ID
                startActivity(intent)
            },
            onDeleteClicked = { product ->
                showDeleteConfirmationDialog(product)
            }
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }
    }

    private fun setupFab() {
        addProductFab.setOnClickListener {
            // Navigate to AdminProductAddEditActivity (without product ID for new product)
            Log.d("AdminProductsFrag", "Add new product FAB clicked")
            val intent = Intent(activity, AdminProductAddEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collectLatest { products ->
                    productAdapter.submitList(products)
                    updateUIStates(products, viewModel.isLoading.value, viewModel.error.value)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    updateUIStates(viewModel.products.value, isLoading, viewModel.error.value)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { error ->
                    updateUIStates(viewModel.products.value, viewModel.isLoading.value, error)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actionFeedback.collectLatest { feedback ->
                    if (feedback != null) {
                        Toast.makeText(context, feedback, Toast.LENGTH_SHORT).show()
                        viewModel.consumeActionFeedback()
                    }
                }
            }
        }
    }

    private fun updateUIStates(products: List<Product>, isLoading: Boolean, error: String?) {
        progressBar.isVisible = isLoading
        if (isLoading) {
            recyclerView.isVisible = false
            messageTextView.isVisible = false
        } else if (error != null) {
            recyclerView.isVisible = false
            messageTextView.isVisible = true
            messageTextView.text = error
        } else if (products.isEmpty()) {
            recyclerView.isVisible = false
            messageTextView.isVisible = true
            messageTextView.text = "No products found. Click '+' to add one."
        } else {
            recyclerView.isVisible = true
            messageTextView.isVisible = false
        }
    }

    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete \"${product.name}\"? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteProduct(product.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
