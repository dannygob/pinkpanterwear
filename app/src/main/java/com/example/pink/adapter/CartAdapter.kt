package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.model.CartItem

class CartAdapter(
    private var items: List<CartItem>,
    private val onDelete: (CartItem) -> Unit,
    private val onUpdate: (CartItem) -> Unit, // New callback for updating item
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.itemName)
        val quantity: TextView = itemView.findViewById(R.id.itemQuantity)
        val price: TextView = itemView.findViewById(R.id.itemPrice)
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteBtn)
        val minusBtn: ImageView = itemView.findViewById(R.id.minusBtn) // New
        val plusBtn: ImageView = itemView.findViewById(R.id.plusBtn)  // New
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.productName
        holder.quantity.text = "x${item.quantity}"
        holder.price.text = "€%.2f".format(item.productPrice * item.quantity)

        holder.deleteBtn.setOnClickListener {
            onDelete(item)
        }

        holder.minusBtn.setOnClickListener {
            if (item.quantity > 1) {
                val updatedItem = item.copy(quantity = item.quantity - 1)
                onUpdate(updatedItem)
            } else {
                // If quantity is 1, delete the item
                onDelete(item)
            }
        }

        holder.plusBtn.setOnClickListener {
            val updatedItem = item.copy(quantity = item.quantity + 1)
            onUpdate(updatedItem)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
