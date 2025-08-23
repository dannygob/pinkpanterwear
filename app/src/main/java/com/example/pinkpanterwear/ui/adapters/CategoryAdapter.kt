package com.example.pinkpanterwear.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R

class CategoryAdapter(
    private val onCategoryClicked: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // Assuming a simple layout item_category.xml with a TextView android:id="@+id/category_name_text_view"
        // If item_category.xml does not exist or is different, this needs adjustment.
        // For now, creating a placeholder R.layout.item_category if it's not there.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = getItem(position)
        holder.bind(categoryName, onCategoryClicked)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Assuming item_category.xml has a TextView with this ID
        private val categoryNameTextView: TextView =
            itemView.findViewById(R.id.category_name_text_view)

        fun bind(categoryName: String, onCategoryClicked: (String) -> Unit) {
            categoryNameTextView.text = categoryName
            itemView.setOnClickListener {
                onCategoryClicked(categoryName)
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}