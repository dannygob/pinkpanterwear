package com.example.slickkwear

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.slickkwear.Model.SliderItem
import com.example.slickkwear.ViewPager2SliderAdapter.SliderViewHolder
import com.squareup.picasso.Picasso

class ViewPager2SliderAdapter(
    private val context: Context,
    private val sliderItems: List<SliderItem>
) :
    RecyclerView.Adapter<SliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val sliderItem = sliderItems[position]
        holder.textViewDescription.text = sliderItem.description
        Picasso.get().load(sliderItem.imageUrl).into(holder.imageViewBackground)

        holder.itemView.setOnClickListener { v: View? ->
            Toast.makeText(
                context,
                "This is item in position $position", Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewBackground: ImageView =
            itemView.findViewById<ImageView>(R.id.iv_auto_image_slider)
        var textViewDescription: TextView =
            itemView.findViewById<TextView>(R.id.tv_auto_image_slider)
    }
}