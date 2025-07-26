package com.example.pink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.adapter.CartAdapter


class UserCartFragment : Fragment() {
    class UserCartFragment : Fragment() {

        private lateinit var viewModel: CartViewModel
        private lateinit var adapter: CartAdapter
        private lateinit var cartRecyclerView: RecyclerView
        private lateinit var totalLabel: TextView
        private lateinit var checkoutButton: Button

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View = inflater.inflate(R.layout.fragment_user_cart, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            cartRecyclerView = view.findViewById(R.id.userCartRecyclerView)
            totalLabel = view.findViewById(R.id.cartTotalLabel)
            checkoutButton = view.findViewById(R.id.checkoutBtn)

            viewModel = ViewModelProvider(this)[CartViewModel::class.java]
            adapter = CartAdapter(emptyList()) { itemToDelete ->
                viewModel.deleteItem(itemToDelete)
            }

            cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cartRecyclerView.adapter = adapter

            viewModel.cartItems.observe(viewLifecycleOwner) { items ->
                adapter.updateItems(items)
                val total = viewModel.getTotal(items)
                totalLabel.text = "Total: €%.2f".format(total)
            }

            checkoutButton.setOnClickListener {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }
    }
}