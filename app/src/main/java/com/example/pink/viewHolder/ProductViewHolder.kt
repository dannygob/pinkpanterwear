package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val txtProductName: TextView = itemView.findViewById(R.id.product_name)
    val txtProductPrice: TextView = itemView.findViewById(R.id.product_price)
    val txtProductDescription: TextView = itemView.findViewById(R.id.product_description)
    val txtProductImage: ImageView = itemView.findViewById(R.id.product_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this) // Activar el listener de clic
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, adapterPosition, false)
    }
}