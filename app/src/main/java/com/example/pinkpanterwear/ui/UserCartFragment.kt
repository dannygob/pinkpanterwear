package com.example.pinkpanterwear.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinkpanterwear.data.CartItem
import com.example.pinkpanterwear.data.Product
import com.google.android.material.textview.MaterialTextView
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale
import com.example.pinkpanterwear.R // Make sure this matches your project's package structure
import android.widget.Button
import com.example.pinkpanterwear.ui.CheckoutPlaceholderFragment
import kotlinx.coroutines.launch

class UserCartFragment : Fragment() {

 private lateinit var cartAdapter: CartAdapter
 private lateinit var cartRecyclerView: RecyclerView
 private lateinit var totalPriceTextView: MaterialTextView
 private lateinit var emptyCartMessage: MaterialTextView
 private val cartItems: MutableList<CartItem> = mutableListOf() // Use a mutable list to easily modify

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_cart, container, false)

        // Basic log statement to confirm onCreateView is called
        Log.d("UserCartFragment", "onCreateView called")

        totalPriceTextView = view.findViewById(R.id.total_price_text_view)
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view)
        emptyCartMessage = view.findViewById(R.id.empty_cart_message)


        // Set the layout manager
        cartRecyclerView.layoutManager = LinearLayoutManager(context)

 // Create the adapter with an empty list initially
 cartAdapter = CartAdapter(emptyList(),
 onQuantityChange = { cartItem, quantityChange ->
 // Call the ViewModel to update the quantity
 viewModel.updateItemQuantity(cartItem.product.id, (cartItem.quantity + quantityChange).coerceAtLeast(1))
 },
 onRemoveItem = { cartItem ->
 // Call the ViewModel to remove the item
 viewModel.removeItem(cartItem.product.id)
        }
 )
        cartRecyclerView.adapter = cartAdapter

 // Observe the cart items from the ViewModel
 lifecycleScope.launch {
 repeatOnLifecycle(Lifecycle.State.STARTED) {
 viewModel.cartItems.collect { cartItems ->
 cartAdapter.submitList(cartItems.toList()) // Use submitList with ListAdapter
 updateTotalPrice(cartItems)
 updateUIState(cartItems)
                    }
                }
            }

        val checkoutButton: Button = view.findViewById(R.id.proceed_to_checkout_button)
        checkoutButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutPlaceholderFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadCartItems(): List<CartItem> {
        // Clear the existing list before loading new data
        cartItems.clear()

        // TODO: Replace with your actual data loading logic for cart items.
        // Fetch the list of CartItem from your persistent storage (database, API, etc.)
        val loadedItems = listOf(
            CartItem(Product("1", "Product A", 10.0, "url_a", "cat_1"), 2),
            CartItem(Product("2", "Product B", 25.0, "url_b", "cat_2"), 1),
            CartItem(Product("3", "Product C", 5.0, "url_c", "cat_1"), 3)
        )
    }
        // Add loaded items to the mutable list
        cartItems.addAll(loadedItems)
        return cartItems // Return the mutable list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCartItems() // Trigger initial data load
    }

    private fun updateTotalPrice(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.product.price * it.quantity }
        // Format the total price as currency
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()) // Use device's default locale
 totalPriceTextView.text = "Total: ${currencyFormat.format(total)}"
    }

    private fun updateUIState(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) {
            cartRecyclerView.visibility = View.GONE
            totalPriceTextView.visibility = View.GONE
 emptyCartMessage.visibility = View.VISIBLE
        } else {
 cartRecyclerView.visibility = View.VISIBLE
 totalPriceTextView.visibility = View.VISIBLE
 emptyCartMessage.visibility = View.GONE
        }
    }
}