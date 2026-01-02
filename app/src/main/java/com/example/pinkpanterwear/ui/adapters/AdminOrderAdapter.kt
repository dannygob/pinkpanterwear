package com.example.pinkpanterwear.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
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

            // ORDER ID
            orderIdTextView.text = "#${order.orderId}"

            // USER NAME
            userNameTextView.text = order.userName

            // TOTAL AMOUNT
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            try {
                currencyFormat.currency = Currency.getInstance("USD")
            } catch (_: Exception) {
            }
            totalTextView.text = currencyFormat.format(order.totalAmount)

            // STATUS LABEL (DYNAMIC)
            val context = itemView.context
            val status = order.orderStatus.lowercase()

            when (status) {

                "pending" -> {
                    statusTextView.text = context.getString(R.string.status_pending)
                    statusTextView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_status_pending)
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
                }

                "delivered" -> {
                    statusTextView.text = context.getString(R.string.status_delivered)
                    statusTextView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_status_delivered)
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                "cancelled" -> {
                    statusTextView.text = context.getString(R.string.status_cancelled)
                    statusTextView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_status_cancelled)
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                "returned" -> {
                    statusTextView.text = context.getString(R.string.status_returned)
                    statusTextView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_status_returned)
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
                }

                else -> {
                    statusTextView.text = order.orderStatus
                    statusTextView.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_status_pending)
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            // DATE
            if (order.orderDate != null) {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    dateTextView.text = sdf.format(order.orderDate.toDate())
                } catch (_: Exception) {
                    dateTextView.text = context.getString(R.string.date_not_available)
                }
            } else {
                dateTextView.text = context.getString(R.string.date_not_available)
            }

            // CLICK LISTENER
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
            return oldItem == newItem
        }
    }
}