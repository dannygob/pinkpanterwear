package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pinkpanterwear.R

class UserHelpFragment : Fragment() {

    private val TAG = "UserHelpFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_help, container, false)

        // Get references to the help views
        view.findViewById<TextView>(R.id.help_title)
        view.findViewById<TextView>(R.id.help_content)

        // Get references to the FAQ views
        view.findViewById<TextView>(R.id.faq_title)
        view.findViewById<TextView>(R.id.faq_q1)
        view.findViewById<TextView>(R.id.faq_a1)

        // TODO: Load dynamic help content here if needed

        return view
    }
}
