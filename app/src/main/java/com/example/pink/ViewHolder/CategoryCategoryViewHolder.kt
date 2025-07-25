package com.example.pink.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R

class CategoryCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val txtCategoryName: TextView = itemView.findViewById(R.id.user_home_category_name)
    val txtCategoryImage: ImageView = itemView.findViewById(R.id.user_home_category_image)
}