package com.example.pinkpanterwear.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.pinkpanterwear.R

class UserCategoryFragment : Fragment() {

    // Assume a simple data class Category exists for now

    private val TAG = "UserCategoryFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_user_category, container, false)

        val categoriesRecyclerView: RecyclerView = view.findViewById(R.id.categories_recycler_view)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Sample Category data for testing
        val sampleCategories = listOf(
            Category("1", "Electronics"),
            Category("2", "Clothing"),
            Category("3", "Books"),
            Category("4", "Home & Garden")
        )

        // Create an instance of CategoryAdapter and pass the click listener
        val categoryAdapter = CategoryAdapter(sampleCategories) { category ->
            // Handle category item click
            Log.d(TAG, "Category clicked: ${category.name}")

            // Navigate to UserCategoryProductsFragment
            parentFragmentManager.commit {
                replace(R.id.fragment_container, UserCategoryProductsFragment.newInstance(category.id))
                addToBackStack(null) // Optional: allows navigating back to the category list
            }
        }
        categoriesRecyclerView.adapter = categoryAdapter
        return view
    }
}