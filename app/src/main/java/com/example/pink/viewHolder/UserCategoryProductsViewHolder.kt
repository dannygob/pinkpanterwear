package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class UserCategoryProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val txtCategoryProductsName: TextView =
        itemView.findViewById(R.id.user_category_products_name)
    private val txtCategoryProductsPrice: TextView =
        itemView.findViewById(R.id.user_category_products_price)
    private val txtCategoryProductsImage: ImageView =
        itemView.findViewById(R.id.user_category_products_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(name: String, price: String, imageUrl: String) {
        txtCategoryProductsName.text = name
        txtCategoryProductsPrice.text = price
        // Glide.with(itemView.context).load(imageUrl).into(txtCategoryProductsImage)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}