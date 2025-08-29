package com.example.pinkpanterwear.ui.Fragment

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
import com.example.pinkpanterwear.entities.Order
import com.example.pinkpanterwear.ui.ViewModel.AdminOrdersViewModel
import com.example.pinkpanterwear.ui.adapters.AdminOrderAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminAllOrdersFragment : Fragment() {

    private val viewModel: AdminOrdersViewModel by viewModels()
    private lateinit var adminOrderAdapter: AdminOrderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView // For errors or empty list

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_all_orders, container, false)
        recyclerView = view.findViewById(R.id.admin_orders_recycler_view)
        progressBar = view.findViewById(R.id.admin_orders_progress_bar)
        messageTextView = view.findViewById(R.id.admin_orders_message_text_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Optional: Add SwipeRefreshLayout and trigger viewModel.fetchAllOrders()
    }

    private fun setupRecyclerView() {
        adminOrderAdapter = AdminOrderAdapter { order ->
            // TODO: Navigate to Admin Order Details screen
            Toast.makeText(context, "Clicked Order ID: ${order.orderId}", Toast.LENGTH_SHORT).show()
            Log.d("AdminAllOrders", "Clicked on order: ${order.orderId}")
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adminOrderAdapter
            // Add item dividers if desired:
            // addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orders.collectLatest { orders ->
                    adminOrderAdapter.submitList(orders as List<Order?>?)
                    updateUIStates(orders, viewModel.isLoading.value, viewModel.error.value)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    updateUIStates(viewModel.orders.value, isLoading, viewModel.error.value)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { error ->
                    updateUIStates(viewModel.orders.value, viewModel.isLoading.value, error)
                }
            }
        }
    }

    private fun updateUIStates(orders: List<Order>, isLoading: Boolean, error: String?) {
        progressBar.isVisible = isLoading

        if (isLoading) {
            recyclerView.isVisible = false
            messageTextView.isVisible = false
        } else if (error != null) {
            recyclerView.isVisible = false
            messageTextView.isVisible = true
            messageTextView.text = error
        } else if (orders.isEmpty()) {
            recyclerView.isVisible = false
            messageTextView.isVisible = true
            messageTextView.text = "No orders found."
        } else {
            recyclerView.isVisible = true
            messageTextView.isVisible = false
        }
    }
}
