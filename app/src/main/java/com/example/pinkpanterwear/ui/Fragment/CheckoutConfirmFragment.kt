package com.example.pinkpanterwear.ui.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.ui.ViewModel.CheckoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class CheckoutConfirmFragment : Fragment() {

    private lateinit var shippingAddressDisplayTextView: TextView
    private lateinit var orderItemsContainer: LinearLayout
    private lateinit var grandTotalTextView: TextView
    private lateinit var placeOrderButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    private val checkoutViewModel: CheckoutViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout_confirm, container, false)
        shippingAddressDisplayTextView = view.findViewById(R.id.shipping_address_display_text_view)
        orderItemsContainer = view.findViewById(R.id.order_items_container_linear_layout)
        grandTotalTextView = view.findViewById(R.id.grand_total_text_view)
        placeOrderButton = view.findViewById(R.id.place_order_button)
        progressBar = view.findViewById(R.id.confirm_progress_bar) // For order placement
        errorTextView =
            view.findViewById(R.id.confirm_error_text_view) // For order placement errors

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Confirm Order"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request cart summary load when view is created if not already loaded by VM
        // ViewModel might already hold it if user navigates back and forth
        if (checkoutViewModel.cartItemsForSummary.value.isEmpty()) {
            checkoutViewModel.loadCartForSummary()
        }

        observeViewModel()

        placeOrderButton.setOnClickListener {
            checkoutViewModel.placeOrder()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.shippingAddress.collectLatest { addressMap ->
                    displayShippingAddress(addressMap)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe cart items for summary display
                checkoutViewModel.cartItemsForSummary.collectLatest { items ->
                    populateOrderItemsUI(items)
                    // Update grand total when items change
                    checkoutViewModel.grandTotal.value.let { displayGrandTotal(it) }
                    placeOrderButton.isEnabled =
                        items.isNotEmpty() && checkoutViewModel.shippingAddress.value != null
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.grandTotal.collectLatest { total ->
                    displayGrandTotal(total)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.isLoadingCartSummary.collectLatest { isLoading ->
                    // Could show a different progress bar for summary loading if needed
                    // For now, main progress bar might cover it, or rely on cart screen's loading
                    if (isLoading) {
                        orderItemsContainer.removeAllViews()
                        TextView(context).apply { text = "Loading cart summary..." }
                            .also { orderItemsContainer.addView(it) }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.cartSummaryError.collectLatest { error ->
                    if (error != null) {
                        orderItemsContainer.removeAllViews()
                        TextView(context).apply {
                            text = error
                            setTextColor(
                                resources.getColor(
                                    R.color.error_color,
                                    null
                                )
                            ) // Use theme error color
                        }.also { orderItemsContainer.addView(it) }
                        placeOrderButton.isEnabled = false
                        checkoutViewModel.consumeCartSummaryError()
                    }
                }
            }
        }

        // Observe order placement states
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.isLoadingOrderPlacement.collectLatest { isLoading ->
                    progressBar.isVisible = isLoading
                    placeOrderButton.isEnabled = !isLoading // Disable button while placing order
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.orderPlacementError.collectLatest { error ->
                    if (error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        errorTextView.text = error
                        errorTextView.isVisible = true
                        checkoutViewModel.consumeOrderPlacementStatus()
                    } else {
                        errorTextView.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.orderPlacementSuccess.collectLatest { orderId ->
                    if (orderId != null) {
                        Toast.makeText(
                            context,
                            "Order Placed Successfully! Order ID: $orderId",
                            Toast.LENGTH_LONG
                        ).show()
                        checkoutViewModel.consumeOrderPlacementStatus()
                        // Navigate to success screen or home, clearing backstack appropriately
                        parentFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )

                        // Navigate to OrderSuccessFragment if it exists, else HomeFragment
                        val successFragment = OrderSuccessFragment.newInstance(orderId)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, successFragment)
                            // No addToBackStack, this is a final step in this flow
                            .commit()
                    }
                }
            }
        }
    }

    private fun displayShippingAddress(addressMap: Map<String, String>?) {
        addressMap?.let {
            val name = it["fullName"] ?: ""
            val street = it["street"] ?: ""
            val city = it["city"] ?: ""
            val zip = it["zip"] ?: ""
            val country = it["country"] ?: ""
            shippingAddressDisplayTextView.text = "$name\n$street\n$city, $zip\n$country"
        } ?: run {
            shippingAddressDisplayTextView.text = "Shipping address not provided."
        }
    }

    private fun populateOrderItemsUI(items: List<CartItem>) {
        orderItemsContainer.removeAllViews()
        if (items.isEmpty() && !checkoutViewModel.isLoadingCartSummary.value) { // Check loading state too
            TextView(context).apply { text = "Cart is empty." }
                .also { orderItemsContainer.addView(it) }
            return
        }
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = Currency.getInstance("USD")
        }
        items.forEach { cartItem ->
            val itemTextView = TextView(context).apply {
                text = "${cartItem.quantity} x ${cartItem.product.name} @ ${
                    currencyFormat.format(cartItem.product.price)
                } = ${currencyFormat.format(cartItem.product.price * cartItem.quantity)}"
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // setTextAppearance(android.R.style.TextAppearance_Material_Body2) // Deprecated, use theme attributes
                setPadding(0, 4, 0, 4)
            }
            orderItemsContainer.addView(itemTextView)
        }
    }

    private fun displayGrandTotal(total: Double) {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = Currency.getInstance("USD")
        }
        grandTotalTextView.text = "Total: ${currencyFormat.format(total)}"
    }
}