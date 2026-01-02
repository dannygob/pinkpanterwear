package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinkpanterwear.databinding.FragmentAdminReturnedOrdersBinding
import com.example.pinkpanterwear.ui.adapters.AdminOrderAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminReturnedOrdersFragment : Fragment() {

    private var _binding: FragmentAdminReturnedOrdersBinding? = null
    private val binding get() = _binding!!

    private val adapter = AdminOrderAdapter { /* handle click */ }
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReturnedOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adminReturnedOrdersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.adminReturnedOrdersRecyclerView.adapter = adapter

        loadReturnedOrders()
    }

    private fun loadReturnedOrders() {
        binding.adminReturnedOrdersProgressBar.visibility = View.VISIBLE

        db.collection("orders")
            .whereEqualTo("orderStatus", "returned")
            .get()
            .addOnSuccessListener { result ->
                val orders = result.toObjects(com.example.pinkpanterwear.entities.Order::class.java)
                adapter.submitList(orders)

                binding.adminReturnedOrdersProgressBar.visibility = View.GONE

                binding.adminReturnedOrdersMessageTextView.visibility =
                    if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                binding.adminReturnedOrdersProgressBar.visibility = View.GONE
                binding.adminReturnedOrdersMessageTextView.visibility = View.VISIBLE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}