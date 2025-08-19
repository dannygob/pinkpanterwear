package com.example.pink.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pink.R
import com.example.pink.model.Products

class ProductAsAdapter(
    private val products: List<Products>,
    private val onAddToCart: (Products) -> Unit,
) : RecyclerView.Adapter<ProductAsAdapter.ProductViewHolder>() {

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
        Glide.with(holder.itemView.context).load(product.productImage).into(holder.image)
        holder.addBtn.setOnClickListener {
            onAddToCart(product)
        }
    }

    override fun getItemCount(): Int = products.size
}
