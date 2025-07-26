package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.model.Categories
import com.squareup.picasso.Picasso

class CategoriesAdapter(
    private val onCardClick: (Categories) -> Unit,
    private val onButtonClick: (Categories) -> Unit,
) : PagingDataAdapter<Categories, CategoriesAdapter.CategoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_category_card_layout, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        getItem(position)?.let { category ->
            holder.bind(category, onCardClick, onButtonClick)
        }
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.categoryName)
        private val image: ImageView = itemView.findViewById(R.id.categoryImage)
        private val button: Button = itemView.findViewById(R.id.viewProductsButton)

        fun bind(
            category: Categories,
            onCardClick: (Categories) -> Unit,
            onButtonClick: (Categories) -> Unit,
        ) {
            name.text = category.categoryName
            Picasso.get()
                .load(category.categoryImage)
                .placeholder(R.drawable.ic_baseline_insert_photo_24)
                .error(R.drawable.ic_error)
                .into(image)

            itemView.setOnClickListener {
                onCardClick(category)
            }

            button.setOnClickListener {
                onButtonClick(category)
            }
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