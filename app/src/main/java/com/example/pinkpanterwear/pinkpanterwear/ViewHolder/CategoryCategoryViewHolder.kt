package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.slickkwear.Interface.ItemClickListener
import com.example.slickkwear.R


class CategoryCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    @JvmField
    var txtCategoryName: TextView =
        itemView.findViewById<View>(R.id.user_home_category_name) as TextView

    @JvmField
    var txtCategoryImage: ImageView =
        itemView.findViewById<View>(R.id.user_home_category_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener?.onClick(view, getAdapterPosition(), false)
    }
}
