package com.example.pink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.pink.R
import com.example.pink.adapter.SliderHomeAdapter

class UserHomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)

        viewPager = view.findViewById(R.id.user_home_deals_image)

        // Lista de URLs de imágenes
        val imageUrls = getImageUrls()

        sliderAdapter = SliderHomeAdapter(imageUrls)
        viewPager.adapter = sliderAdapter

        return view
    }

    // Placeholder function to simulate fetching image URLs from an API or database
    // TODO: Replace with a real implementation
    private fun getImageUrls(): List<String> {
        return listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )
    }
}
