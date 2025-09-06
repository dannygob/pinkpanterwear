package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pink.R
import com.example.pink.model.Products

class ProductAdapter(
    private val products: MutableList<Products>,
    private val onAddToCart: (Products) -> Unit,
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.productName)
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val addBtn: Button = itemView.findViewById(R.id.addToCartBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.productName
        holder.price.text = "€%.2f".format(product.productPrice)

        Glide.with(holder.itemView.context)
            .load(product.productImage)
            .placeholder(R.drawable.ic_baseline_insert_photo_24)
            .error(R.drawable.ic_error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        holder.addBtn.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int = products.size

    // 🔄 Método para actualizar la lista si se usa submitList manual
    fun updateList(newList: List<Products>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }

    // 🧭 ItemTouchHelper para deslizamiento
    fun getSwipeHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val product = products[position]
                onAddToCart(product)
                notifyItemChanged(position) // Restaurar visualmente el ítem
            }
        })
    }
}
