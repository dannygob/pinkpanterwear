package com.example.pink.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.squareup.picasso.Picasso

class SliderHomeAdapter(
    private val imageUrls: List<String>,
    private val onItemClick: ((String) -> Unit)? = null,
) : RecyclerView.Adapter<SliderHomeAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.slider_image)

        fun bind(imageUrl: String) {
            Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.image_placeholder_rounded)
                .error(R.drawable.ic_error)
                .tag("SliderHomeAdapter")
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick?.invoke(imageUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size
}