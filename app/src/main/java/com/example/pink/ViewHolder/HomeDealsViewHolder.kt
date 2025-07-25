package com.example.pink.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeDealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtDealName: TextView?
    var txtDealPriceInitial: TextView?
    var txtDealPriceDiscounted: TextView?
    var txtDealImage: ImageView?
    var listener: ItemClickListener? = null

    init {
        txtDealImage = itemView.findViewById<View?>(R.id.user_home_deals_image) as ImageView?
        txtDealName = itemView.findViewById<View?>(R.id.user_home_deals_name) as TextView?
        txtDealPriceInitial =
            itemView.findViewById<View?>(R.id.user_home_deals_price_initial) as TextView?
        txtDealPriceDiscounted =
            itemView.findViewById<View?>(R.id.user_home_deals_price_discounted) as TextView?
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener!!.onClick(view, adapterPosition, false)
    }
}
