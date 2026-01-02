package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinkpanterwear.databinding.FragmentAdminDeliveredOrdersBinding
import com.example.pinkpanterwear.ui.adapters.AdminOrderAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminDeliveredOrdersFragment : Fragment() {

    private var _binding: FragmentAdminDeliveredOrdersBinding? = null
    private val binding get() = _binding!!

    private val adapter = AdminOrderAdapter { /* TODO: handle order click if needed */ }
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDeliveredOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adminDeliveredOrdersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.adminDeliveredOrdersRecyclerView.adapter = adapter

        loadDeliveredOrders()
    }

    private fun loadDeliveredOrders() {
        binding.adminDeliveredOrdersProgressBar.visibility = View.VISIBLE
        binding.adminDeliveredOrdersMessageTextView.visibility = View.GONE

        db.collection("orders")
            .whereEqualTo("orderStatus", "delivered")
            .get()
            .addOnSuccessListener { result ->
                val orders =
                    result.toObjects(com.example.pinkpanterwear.entities.Order::class.java)

                adapter.submitList(orders)

                binding.adminDeliveredOrdersProgressBar.visibility = View.GONE
                binding.adminDeliveredOrdersMessageTextView.visibility =
                    if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                binding.adminDeliveredOrdersProgressBar.visibility = View.GONE
                binding.adminDeliveredOrdersMessageTextView.visibility = View.VISIBLE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}