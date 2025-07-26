package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val txtCategoryName: TextView = itemView.findViewById(R.id.admin_category_layout_name)
    val txtCategoryStatus: TextView =
        itemView.findViewById(R.id.admin_category_layout_status)
    private val txtCategoryImage: ImageView =
        itemView.findViewById(R.id.admin_category_layout_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)  // ⏫ Registramos el click directamente
    }

    fun bind(categoryName: String, categoryStatus: String, imageUrl: String) {
        txtCategoryName.text = categoryName
        txtCategoryStatus.text = categoryStatus
        // Aquí puedes usar Glide, Coil o Picasso para cargar la imagen
        // Glide.with(itemView.context).load(imageUrl).into(txtCategoryImage)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}