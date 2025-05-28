package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.slickkwear.R

class HomeTrendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtTrendingName: TextView =
        itemView.findViewById<View>(R.id.user_home_trending_name) as TextView
    var txtTrendingPrice: TextView =
        itemView.findViewById<View>(R.id.user_home_trending_price) as TextView
    var txtTrendingImage: ImageView =
        itemView.findViewById<View>(R.id.user_home_trending_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener.onClick(view, getAdapterPosition(), false)
    }
}
