package com.example.pink.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pink.fragment.AdminAllOrdersFragment
import com.example.pink.fragment.AdminDispatchedOrdersFragment
import com.example.pink.fragment.AdminNewOrdersFragment


class AdminOrdersPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminNewOrdersFragment()
            1 -> AdminDispatchedOrdersFragment()
            else -> AdminAllOrdersFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
