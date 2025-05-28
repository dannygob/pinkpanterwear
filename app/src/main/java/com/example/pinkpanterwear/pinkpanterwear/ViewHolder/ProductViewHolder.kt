package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.slickkwear.R

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtProductName: TextView =
        itemView.findViewById<View>(R.id.product_name) as TextView
    var txtProductPrice: TextView =
        itemView.findViewById<View>(R.id.product_price) as TextView
    var txtProductDescription: TextView =
        itemView.findViewById<View>(R.id.product_description) as TextView
    var txtProductImage: ImageView =
        itemView.findViewById<View>(R.id.product_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener.onClick(view, getAdapterPosition(), false)
    }
}
