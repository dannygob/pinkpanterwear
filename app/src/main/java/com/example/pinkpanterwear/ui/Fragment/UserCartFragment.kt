package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class UserCartFragment : Fragment() {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: MaterialTextView
    private lateinit var emptyCartMessage: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var checkoutButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_cart, container, false)

        cartRecyclerView = view.findViewById(R.id.cart_recycler_view)
        totalPriceTextView = view.findViewById(R.id.total_price_text_view)
        emptyCartMessage = view.findViewById(R.id.empty_cart_message)
        progressBar = view.findViewById(R.id.cart_progress_bar)
        errorTextView = view.findViewById(R.id.cart_error_text_view)
        checkoutButton = view.findViewById(R.id.proceed_to_checkout_button) // From previous step

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupCheckoutButton() // Listener was added in previous step, ensure it's still there or re-add
        observeViewModel()

        // viewModel.loadCartItems() // ViewModel's init block already calls this
    }

    override fun onResume() {
        super.onResume()
        // Refresh cart items in case user logged in/out or cart changed in background
        viewModel.loadCartItems()
    }

    private fun setupAdapter() {
        cartAdapter = CartAdapter(
            onIncreaseQuantity = { cartItem ->
                viewModel.updateItemQuantity(cartItem.product.id, cartItem.quantity + 1)
            },
            onDecreaseQuantity = { cartItem ->
                if (cartItem.quantity > 0) { // Ensure quantity doesn't go below 0 from UI before repo handles it
                    viewModel.updateItemQuantity(cartItem.product.id, cartItem.quantity - 1)
                }
            },
            onRemoveItem = { cartItem ->
                viewModel.removeItem(cartItem.product.id)
            }
        )
    }

    private fun setupRecyclerView() {
        cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupCheckoutButton() {
        checkoutButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutShippingFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collectLatest { items ->
                    cartAdapter.submitList(items)
                    updateTotalPrice(items)
                    updateUIStateBasedOnCart(
                        items,
                        viewModel.isLoading.value,
                        viewModel.error.value
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    updateUIStateBasedOnCart(
                        viewModel.cartItems.value,
                        isLoading,
                        viewModel.error.value
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collectLatest { error ->
                    updateUIStateBasedOnCart(
                        viewModel.cartItems.value,
                        viewModel.isLoading.value,
                        error
                    )
                    if (error != null && !viewModel.isLoading.value) { // Only show error if not loading
                        errorTextView.text = error
                    }
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

    private fun updateUIStateBasedOnCart(
        items: List<CartItem>,
        isLoading: Boolean,
        error: String?
    ) {
        progressBar.isVisible = isLoading
        errorTextView.isVisible = error != null && !isLoading

        if (isLoading || error != null) {
            cartRecyclerView.isVisible = false
            totalPriceTextView.isVisible = false
            emptyCartMessage.isVisible = false
            checkoutButton.isVisible = false
        } else {
            cartRecyclerView.isVisible = items.isNotEmpty()
            totalPriceTextView.isVisible = items.isNotEmpty()
            emptyCartMessage.isVisible = items.isEmpty()
            checkoutButton.isVisible = items.isNotEmpty()
        }
    }

    private fun updateTotalPrice(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.product.price * it.quantity }
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        totalPriceTextView.text = "Total: ${currencyFormat.format(total)}"
    }
}