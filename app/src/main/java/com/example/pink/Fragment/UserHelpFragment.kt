package com.example.pink.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pink.R

class UserHelpFragment : Fragment() {
    private var view: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_help, container, false)


        return view
    }
}