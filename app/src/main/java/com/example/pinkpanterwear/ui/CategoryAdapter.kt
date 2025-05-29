package com.example.pinkpanterwear.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R

// Assume this data class exists somewhere in your project
// data class Category(val id: String, val name: String, val imageUrl: String? = null)

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Placeholder for the Category data class if it doesn't exist yet
        data class Category(val id: String, val name: String, val imageUrl: String? = null)


        private val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name_text_view)
        private val categoryImageView: ImageView = itemView.findViewById(R.id.category_image_view) // Placeholder

        fun bind(category: Category) {
            categoryNameTextView.text = category.name
            // TODO: Load image using a library like Glide or Coil
            // if (category.imageUrl != null) {
            //     // Load image into categoryImageView
            // } else {
            //     // Set a default placeholder image
            // }

            itemView.setOnClickListener {
                // The actual Category object will be passed from onBindViewHolder
            }
        }
    }
}