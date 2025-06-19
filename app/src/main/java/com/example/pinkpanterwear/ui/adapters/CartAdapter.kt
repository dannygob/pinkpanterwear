package com.example.pinkpanterwear.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class CartAdapter(
    private val onIncreaseQuantity: (CartItem) -> Unit,
    private val onDecreaseQuantity: (CartItem) -> Unit,
    private val onRemoveItem: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem, onIncreaseQuantity, onDecreaseQuantity, onRemoveItem)
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImageView: ImageView = itemView.findViewById(R.id.cart_item_image)
        private val itemNameTextView: TextView = itemView.findViewById(R.id.cart_item_name)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.cart_item_price)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.cart_item_quantity)
        private val increaseQtyButton: Button =
            itemView.findViewById(R.id.cart_item_increase_qty_button)
        private val decreaseQtyButton: Button =
            itemView.findViewById(R.id.cart_item_decrease_qty_button)
        private val removeItemButton: ImageButton =
            itemView.findViewById(R.id.cart_item_remove_button)

        fun bind(
            cartItem: CartItem,
            onIncreaseQuantity: (CartItem) -> Unit,
            onDecreaseQuantity: (CartItem) -> Unit,
            onRemoveItem: (CartItem) -> Unit
        ) {
            itemNameTextView.text = cartItem.product.name
            itemQuantityTextView.text = cartItem.quantity.toString()

            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = Currency.getInstance("USD") // Or use device locale
            itemPriceTextView.text =
                format.format(cartItem.product.price * cartItem.quantity) // Display total for this item line

            Glide.with(itemView.context)
                .load(cartItem.product.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(itemImageView)

            increaseQtyButton.setOnClickListener { onIncreaseQuantity(cartItem) }
            decreaseQtyButton.setOnClickListener { onDecreaseQuantity(cartItem) }
            removeItemButton.setOnClickListener { onRemoveItem(cartItem) }
        }
    }

    class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem // Relies on CartItem being a data class
        }
    }
}