package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.model.SliderItem
import com.squareup.picasso.Picasso

class SliderAdapter(
    private val imageList: List<SliderItem>,
    private val onClick: ((SliderItem) -> Unit)? = null,
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sliderImage: ImageView = itemView.findViewById(R.id.slider_image)

        fun bind(item: SliderItem) {
            Picasso.get()
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_baseline_insert_photo_24)
                .error(R.drawable.ic_error)
                .into(sliderImage)

            itemView.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = imageList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = imageList.size
}