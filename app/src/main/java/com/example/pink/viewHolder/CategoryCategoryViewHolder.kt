package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.Glide
import com.example.pink.R
import com.example.pink.model.Categories

class CategoryCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtCategoryName: TextView = itemView.findViewById(R.id.user_home_category_name)
    val txtCategoryImage: ImageView = itemView.findViewById(R.id.user_home_category_image)

    fun bind(category: Categories) {
        txtCategoryName.text = category.categoryName
        Glide.with(itemView.context)
            .load(category.imageUrl)
            .into(txtCategoryImage)
    }
}
