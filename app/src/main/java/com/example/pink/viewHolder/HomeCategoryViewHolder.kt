package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val txtCategoryName: TextView = itemView.findViewById(R.id.user_home_category_name)
    private val txtCategoryImage: ImageView = itemView.findViewById(R.id.user_home_category_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this) // Asignamos el click directamente al ViewHolder
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun bind(categoryName: String, imageUrl: String) {
        txtCategoryName.text = categoryName
        // Glide, Coil, o Picasso para cargar la imagen:
        // Glide.with(itemView.context).load(imageUrl).into(txtCategoryImage)
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}