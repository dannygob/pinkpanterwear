package com.example.pinkpanterwear.ui.Fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class OrderSuccessFragment : Fragment() {

    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(ARG_ORDER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_success, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Order Confirmed"
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false) // No back from success

        val orderIdTextView: TextView = view.findViewById(R.id.order_success_order_id_text_view)
        val continueShoppingButton: Button = view.findViewById(R.id.continue_shopping_button)

        orderIdTextView.text = if (!orderId.isNullOrBlank()) {
            "Your Order ID: #${orderId}"
        } else {
            "Your order has been placed." // Fallback if ID is somehow not passed
        }

        continueShoppingButton.setOnClickListener {
            // Clear the entire back stack up to the root/MainActivity's initial fragment (HomeFragment)
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            // Replace with HomeFragment
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    HomeFragment()
                ) // Assuming R.id.fragment_container
                .commit()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore back button if it was globally hidden for this screen
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    companion object {
        private const val ARG_ORDER_ID = "order_id"

        @JvmStatic
        fun newInstance(orderId: String) =
            OrderSuccessFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ORDER_ID, orderId)
                }
            }
    }
}