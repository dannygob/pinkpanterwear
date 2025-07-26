package com.example.pink.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Interface.ItemClickListener
import com.example.pink.R

class HomeDealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val txtDealName: TextView = itemView.findViewById(R.id.user_home_deals_name)
    private val txtDealPriceInitial: TextView =
        itemView.findViewById(R.id.user_home_deals_price_initial)
    private val txtDealPriceDiscounted: TextView =
        itemView.findViewById(R.id.user_home_deals_price_discounted)
    private val txtDealImage: ImageView = itemView.findViewById(R.id.user_home_deals_image)

    private var listener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(dealName: String, initialPrice: String, discountedPrice: String, imageUrl: String) {
        txtDealName.text = dealName
        txtDealPriceInitial.text = initialPrice
        txtDealPriceDiscounted.text = discountedPrice
        // Usa Glide, Coil o Picasso para la imagen
        // Glide.with(itemView.context).load(imageUrl).into(txtDealImage)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener?.onClick(view, bindingAdapterPosition, false)
    }
}