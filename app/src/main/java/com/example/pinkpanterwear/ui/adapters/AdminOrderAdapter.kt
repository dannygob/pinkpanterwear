package com.example.pinkpanterwear.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.dynamicanimation.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.entities.Order
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

class AdminOrderAdapter(
    private val onItemClicked: (Order) -> Unit
) : ListAdapter<Order, AdminOrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order, onItemClicked)
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.admin_order_id_text_view)
        private val userNameTextView: TextView =
            itemView.findViewById(R.id.admin_order_user_name_text_view)
        private val totalTextView: TextView =
            itemView.findViewById(R.id.admin_order_total_text_view)
        private val statusTextView: TextView =
            itemView.findViewById(R.id.admin_order_status_text_view)
        private val dateTextView: TextView = itemView.findViewById(R.id.admin_order_date_text_view)

        fun bind(order: Order, onItemClicked: (Order) -> Unit) {
            orderIdTextView.text = "#${order.orderId}" // Assuming orderId doesn't already have #
            userNameTextView.text = order.userName // Or order.userId if name is not available

            val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            // TODO: Consider storing currency code with order or using a global app setting
            try {
                currencyFormat.currency = Currency.getInstance("USD")
            } catch (e: Exception) { /* Handle cases where USD might not be available, though unlikely */
            }
            totalTextView.text = currencyFormat.format(order.totalAmount)

            statusTextView.text = order.orderStatus
            // TODO: Implement dynamic background tint for statusTextView based on order.orderStatus

            if (order.orderDate != null) {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    // sdf.timeZone = TimeZone.getTimeZone("UTC") // If dates are stored in UTC
                    dateTextView.text = sdf.format(order.orderDate.toDate())
                } catch (e: Exception) {
                    dateTextView.text = "Date N/A"
                }
            } else {
                dateTextView.text = "Date N/A"
            }

            itemView.setOnClickListener {
                onItemClicked(order)
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem // Order is a data class
        }
    }
}