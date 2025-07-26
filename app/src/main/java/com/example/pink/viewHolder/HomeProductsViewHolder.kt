package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val txtProductsName: TextView = itemView.findViewById(R.id.user_home_products_name)
    private val txtProductsPrice: TextView = itemView.findViewById(R.id.user_home_products_price)
    private val txtProductsImage: ImageView = itemView.findViewById(R.id.user_home_products_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(productName: String, price: String, imageUrl: String) {
        txtProductsName.text = productName
        txtProductsPrice.text = price
        // Glide.with(itemView.context).load(imageUrl).into(txtProductsImage) ← activo si usas imágenes online
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}