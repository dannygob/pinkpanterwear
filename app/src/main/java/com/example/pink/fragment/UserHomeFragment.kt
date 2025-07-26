package com.example.pink.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.pink.R
import com.example.pink.adapter.SliderHomeAdapter

class UserHomeFragment : Fragment() {

    private lateinit var imageSlider: ViewPager2
    private lateinit var sliderAdapter: SliderHomeAdapter
    private val sliderHandler = Handler(Looper.getMainLooper())

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var recyclerViewTodayDeals: RecyclerView
    private lateinit var recyclerViewTrending: RecyclerView
    private lateinit var recyclerViewSuggested: RecyclerView

    private val sliderRunnable = Runnable {
        imageSlider.currentItem = (imageSlider.currentItem + 1) % sliderAdapter.itemCount
        sliderHandler.postDelayed(sliderRunnable, 4000)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔘 Slider de imágenes
        imageSlider = view.findViewById(R.id.imageSlider)
        val imageUrls = getImageUrls()
        sliderAdapter = SliderHomeAdapter(imageUrls)
        imageSlider.adapter = sliderAdapter

        // 🔄 Inicia el auto-scroll
        sliderHandler.postDelayed(sliderRunnable, 4000)

        // 🛍️ RecyclerViews principales
        recyclerViewCategory = view.findViewById(R.id.recycler_view_category)
        recyclerViewTodayDeals = view.findViewById(R.id.recycler_view_today_deals)
        recyclerViewTrending = view.findViewById(R.id.recycler_view_home_trending)
        recyclerViewSuggested = view.findViewById(R.id.recycler_view_home_products)

        // 👉 Aquí puedes configurar cada RecyclerView con su Adapter
        // Por ejemplo: recyclerViewCategory.adapter = CategoryAdapter(categoryList)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 4000)
    }

    private fun getImageUrls(): List<String> {
        return listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )
    }
}