package com.example.pink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pink.R

/**
 * A simple [Fragment] subclass.
 * Use the [AdminNewOrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminNewOrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_new_orders, container, false)
    }
}

