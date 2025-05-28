package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.slickkwear.Interface.ItemClickListener
import com.example.slickkwear.R


class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtCategoryName: TextView =
        itemView.findViewById<View>(R.id.admin_category_layout_name) as TextView
    var txtCategoryStatus: TextView =
        itemView.findViewById<View>(R.id.admin_category_layout_status) as TextView
    var txtCategoryImage: ImageView =
        itemView.findViewById<View>(R.id.admin_category_layout_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener?.onClick(view, getAdapterPosition(), false)
    }
}
