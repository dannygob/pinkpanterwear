package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

import com.example.pink.R
import com.example.pink.model.Categories
import com.example.pink.viewHolder.CategoryViewHolder

class CategoryPagingAdapter(
    private val onItemClick: (Categories) -> Unit,
) : PagingDataAdapter<Categories, CategoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_category_display_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.txtCategoryName.text = item.categoryName
        holder.txtCategoryStatus.text = item.categoryStatus

        Glide.with(holder.itemView.context)
            .load(item.categoryImage)
            .into(holder.txtCategoryImage)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Categories>() {
            override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean =
                oldItem.categoryUniqueID == newItem.categoryUniqueID

            override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean =
                oldItem == newItem
        }
    }
}
