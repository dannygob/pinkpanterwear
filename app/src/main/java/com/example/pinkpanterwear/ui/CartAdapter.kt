package com.example.pinkpanterwear.ui

import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.data.CartItem

class CartAdapter(private val cartItems: List<CartItem>,
 private val onQuantityChange: (CartItem, Int) -> Unit,
 private val onRemoveItem: (CartItem) -> Unit) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position] as CartItem // Cast item to your CartItem data class
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name_text_view)
        private val productQuantityTextView: TextView = itemView.findViewById(R.id.product_quantity_text_view)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.product_price_text_view)
        private val decreaseQuantityButton: Button = itemView.findViewById(R.id.decrease_quantity_button)
        private val increaseQuantityButton: Button = itemView.findViewById(R.id.increase_quantity_button)
        private val removeItemButton: ImageView = itemView.findViewById(R.id.remove_item_button)
        private val productImageImageView: ImageView = itemView.findViewById(R.id.product_image_view)

        fun bind(cartItem: CartItem) {
            productNameTextView.text = cartItem.product.name
            productQuantityTextView.text = "Qty: ${cartItem.quantity}"
            productPriceTextView.text = "$${cartItem.product.price}"

            // Load image using a library like Glide or Coil:
            // if (cartItem.imageUrl != null) {
            //     Glide.with(itemView.context).load(cartItem.imageUrl).into(productImageImageView)
            // } else if (cartItem.product.imageUrl != null) {
            //     Glide.with(itemView.context).load(cartItem.product.imageUrl).into(productImageImageView)
            // } else {
            //     productImageImageView.setImageResource(R.drawable.placeholder_image) // Add a placeholder drawable
            // }

            increaseQuantityButton.setOnClickListener {
                onQuantityChange.invoke(cartItem, 1)
            }

            decreaseQuantityButton.setOnClickListener {
                onQuantityChange.invoke(cartItem, -1)
            }

 removeItemButton.setOnClickListener {
 onRemoveItem.invoke(cartItem)
 }

            itemView.setOnClickListener {
                Log.d("CartAdapter", "Clicked on cart item: ${cartItem.product.name}")
                // You can add more complex click handling here, e.g., show details or remove item
            }
        }
    }

}