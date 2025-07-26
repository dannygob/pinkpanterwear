package com.example.pink.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.pink.R
import com.example.pink.adapter.SliderHomeAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AdminAllOrdersFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val imageUrls = listOf(
        "https://example.com/img1.jpg",
        "https://example.com/img2.jpg",
        "https://example.com/img3.jpg"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_all_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPagerSlider)
        tabLayout = view.findViewById(R.id.tabLayoutIndicator)

        viewPager.adapter = SliderHomeAdapter(imageUrls)

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        startAutoSwipe()
    }

    private fun startAutoSwipe() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                currentPage = (currentPage + 1) % imageUrls.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 4000)
            }
        }, 4000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        startAutoSwipe()
    }
}