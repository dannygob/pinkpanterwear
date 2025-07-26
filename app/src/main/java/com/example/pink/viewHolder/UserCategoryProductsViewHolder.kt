package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class UserCategoryProductsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!),
    View.OnClickListener {
    var txtCategoryProductsName: TextView?
    var txtCategoryProductsPrice: TextView?
    var txtCategoryProductsImage: ImageView?
    var listener: ItemClickListener? = null

    init {
        txtCategoryProductsImage =
            itemView?.findViewById<View?>(R.id.user_category_products_image) as ImageView?
        txtCategoryProductsName =
            itemView?.findViewById<View?>(R.id.user_category_products_name) as TextView?
        txtCategoryProductsPrice =
            itemView?.findViewById<View?>(R.id.user_category_products_price) as TextView?
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener!!.onClick(view, adapterPosition, false)
    }
}
