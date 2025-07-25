package com.example.pink.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtProductsName: TextView? =
        itemView.findViewById<View?>(R.id.user_home_products_name) as TextView?
    var txtProductsPrice: TextView? =
        itemView.findViewById<View?>(R.id.user_home_products_price) as TextView?
    var txtProductsImage: ImageView? =
        itemView.findViewById<View?>(R.id.user_home_products_image) as ImageView?
    var listener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener!!.onClick(view, adapterPosition, false)
    }
}
