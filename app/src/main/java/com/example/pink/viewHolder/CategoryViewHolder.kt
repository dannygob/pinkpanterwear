package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pink.R

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val txtCategoryName: TextView = itemView.findViewById(R.id.admin_category_layout_name)
    val txtCategoryStatus: TextView = itemView.findViewById(R.id.admin_category_layout_status)
    val txtCategoryImage: ImageView = itemView.findViewById(R.id.admin_category_layout_image)

    fun bind(name: String, status: String, imageUrl: String) {
        txtCategoryName.text = name
        txtCategoryStatus.text = status

        Glide.with(itemView.context).load(imageUrl).into(txtCategoryImage)
    }
}
