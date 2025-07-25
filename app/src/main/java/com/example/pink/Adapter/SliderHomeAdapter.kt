package com.example.pink.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.squareup.picasso.Picasso

class SliderHomeAdapter(private val sliderItems: List<String>) :
    RecyclerView.Adapter<SliderHomeAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.slider_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val imageUrl = sliderItems[position]
        Picasso.get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.image_placeholder_rounded) // Placeholder image
            .error(R.drawable.ic_error) // Error image
            .tag("SliderHomeAdapter") // Tag for debugging
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = sliderItems.size
}
