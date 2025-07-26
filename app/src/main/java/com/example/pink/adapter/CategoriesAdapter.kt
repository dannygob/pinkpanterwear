package com.example.pink.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.model.Categories
import com.squareup.picasso.Picasso

class CategoriesAdapter(
    private val onClick: (Categories) -> Unit,
) : PagingDataAdapter<Categories, CategoriesAdapter.CategoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_category_category_layout, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener { onClick(it) }
        }
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.txtCategoryName)
        private val image: ImageView = itemView.findViewById(R.id.txtcategoryimage)

        fun bind(category: Categories) {
            name.text = category.categoryName
            Picasso.get().load(category.categoryImage).into(image)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Categories>() {
            override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
                return oldItem.categoryUniqueID == newItem.categoryUniqueID
            }

            override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
                return oldItem == newItem
            }
        }
    }
}
