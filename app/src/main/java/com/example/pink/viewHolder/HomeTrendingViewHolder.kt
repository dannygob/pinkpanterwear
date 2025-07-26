package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeTrendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val txtTrendingName: TextView = itemView.findViewById(R.id.user_home_trending_name)
    private val txtTrendingPrice: TextView = itemView.findViewById(R.id.user_home_trending_price)
    private val txtTrendingImage: ImageView = itemView.findViewById(R.id.user_home_trending_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(name: String, price: String, imageUrl: String) {
        txtTrendingName.text = name
        txtTrendingPrice.text = price
        // Glide.with(itemView.context).load(imageUrl).into(txtTrendingImage)
        // O usa Coil si prefieres una alternativa más ligera
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}