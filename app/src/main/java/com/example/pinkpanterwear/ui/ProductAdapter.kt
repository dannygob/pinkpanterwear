package com.example.pinkpanterwear.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.data.Product

class ProductAdapter(
    private val products: List<Product>,
    private val onItemClick: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name_text_view)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.product_price_text_view)
        private val productImageView: ImageView = itemView.findViewById(R.id.product_image_view)

        fun bind(product: Product) {
            productNameTextView.text = product.name
            productPriceTextView.text = "$${product.price}" // Display price with a dollar sign

            itemView.setOnClickListener {
                onItemClick(product)
            }

            // Load image using a library like Glide or Coil
            // e.g., Glide.with(itemView.context).load(product.imageUrl).into(productImageView)
        }
    }
}