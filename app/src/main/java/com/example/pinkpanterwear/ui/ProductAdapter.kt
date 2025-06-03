package com.example.pinkpanterwear.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Assuming Glide is available (used in UserProductDetailsActivity)
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.R
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onItemClicked: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onItemClicked)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImageView: ImageView = itemView.findViewById(R.id.product_image_view)
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name_text_view)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.product_price_text_view)

        fun bind(product: Product, onItemClicked: (Product) -> Unit) {
            productNameTextView.text = product.name

            // Format price (example)
            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = java.util.Currency.getInstance("USD") // Or use device locale currency
            productPriceTextView.text = format.format(product.price)

            Glide.with(itemView.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(productImageView)

            itemView.setOnClickListener {
                onItemClicked(product)
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem // Relies on Product being a data class
        }
    }
}
