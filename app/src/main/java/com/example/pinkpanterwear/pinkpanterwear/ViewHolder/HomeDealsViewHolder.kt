package com.example.slickkwear.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.slickkwear.R

class HomeDealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtDealName: TextView =
        itemView.findViewById<View>(R.id.user_home_deals_name) as TextView
    var txtDealPriceInitial: TextView =
        itemView.findViewById<View>(R.id.user_home_deals_price_initial) as TextView
    var txtDealPriceDiscounted: TextView =
        itemView.findViewById<View>(R.id.user_home_deals_price_discounted) as TextView
    var txtDealImage: ImageView =
        itemView.findViewById<View>(R.id.user_home_deals_image) as ImageView
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View) {
        listener.onClick(view, getAdapterPosition(), false)
    }
}
