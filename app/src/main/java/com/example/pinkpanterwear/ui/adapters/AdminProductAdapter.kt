package com.example.pinkpanterwear.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.entities.Product
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class AdminProductAdapter(
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit
) : ListAdapter<Product, AdminProductAdapter.AdminProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_product, parent, false)
        return AdminProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, onEditClicked, onDeleteClicked)
    }

    class AdminProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.admin_product_item_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.admin_product_item_name)
        private val categoryTextView: TextView =
            itemView.findViewById(R.id.admin_product_item_category)
        private val priceTextView: TextView = itemView.findViewById(R.id.admin_product_item_price)
        private val idTextView: TextView = itemView.findViewById(R.id.admin_product_item_id)
        private val editButton: ImageButton = itemView.findViewById(R.id.admin_product_edit_button)
        private val deleteButton: ImageButton =
            itemView.findViewById(R.id.admin_product_delete_button)

        fun bind(
            product: Product,
            onEditClicked: (Product) -> Unit,
            onDeleteClicked: (Product) -> Unit
        ) {
            nameTextView.text = product.name
            categoryTextView.text = "Category: ${product.category}"
            idTextView.text = "ID: ${product.id}"

            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = Currency.getInstance("USD")
            priceTextView.text = format.format(product.price)

            Glide.with(itemView.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_placeholder_image) // Placeholder image while loadingZ
                .error(R.drawable.error_image)
                .into(imageView)

            editButton.setOnClickListener { onEditClicked(product) }
            deleteButton.setOnClickListener { onDeleteClicked(product) }
        }
    }

    // Using ProductDiffCallback from ProductAdapter if it's general, or redefine here
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
