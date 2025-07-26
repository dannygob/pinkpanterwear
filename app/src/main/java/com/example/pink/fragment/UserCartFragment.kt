package com.example.pink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pink.R

/**
 * A simple [Fragment] subclass.
 * Use the [UserCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserCartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_user_cart, container, false)
    }
}
}
