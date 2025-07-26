package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtCategoryName: TextView?
    var txtCategoryImage: ImageView?
    var listener: ItemClickListener? = null

    init {
        txtCategoryImage = itemView.findViewById<View?>(R.id.user_home_category_image) as ImageView?
        txtCategoryName = itemView.findViewById<View?>(R.id.user_home_category_name) as TextView?
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener!!.onClick(view, adapterPosition, false)
    }
}
