package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinkpanterwear.databinding.FragmentAdminCancelledOrdersBinding
import com.example.pinkpanterwear.ui.adapters.AdminOrderAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminCancelledOrdersFragment : Fragment() {

    private var _binding: FragmentAdminCancelledOrdersBinding? = null
    private val binding get() = _binding!!

    private val adapter = AdminOrderAdapter { /* TODO: handle order click if needed */ }
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminCancelledOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adminCancelledOrdersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.adminCancelledOrdersRecyclerView.adapter = adapter

        loadCancelledOrders()
    }

    private fun loadCancelledOrders() {
        binding.adminCancelledOrdersProgressBar.visibility = View.VISIBLE
        binding.adminCancelledOrdersMessageTextView.visibility = View.GONE

        db.collection("orders")
            .whereEqualTo("orderStatus", "cancelled")
            .get()
            .addOnSuccessListener { result ->
                val orders =
                    result.toObjects(com.example.pinkpanterwear.entities.Order::class.java)

                adapter.submitList(orders)

                binding.adminCancelledOrdersProgressBar.visibility = View.GONE
                binding.adminCancelledOrdersMessageTextView.visibility =
                    if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                binding.adminCancelledOrdersProgressBar.visibility = View.GONE
                binding.adminCancelledOrdersMessageTextView.visibility = View.VISIBLE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}