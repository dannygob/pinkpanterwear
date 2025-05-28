package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.slickkwear.R

class UserCategoryProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtCategoryProductsName: TextView =
        itemView.findViewById<View>(R.id.user_category_products_name) as TextView
    var txtCategoryProductsPrice: TextView =
        itemView.findViewById<View>(R.id.user_category_products_price) as TextView
    var txtCategoryProductsImage: ImageView =
        itemView.findViewById<View>(R.id.user_category_products_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener.onClick(view, getAdapterPosition(), false)
    }
}
