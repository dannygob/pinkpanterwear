package com.example.pinkpanterwear.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserCategoryFragment : Fragment() {

    private val categoryViewModel: UserCategoryViewModel by viewModels() // Type will now resolve to new import
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_category, container, false)
        recyclerView = view.findViewById(R.id.categories_recycler_view)
        progressBar = view.findViewById(R.id.category_progress_bar)
        errorTextView = view.findViewById(R.id.category_error_text_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter { categoryName ->
            parentFragmentManager.commit {
                replace(
                    R.id.fragment_container,
                    UserCategoryProductsFragment.newInstance(categoryName)
                )
                addToBackStack(null)
            }
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.categories.collectLatest { categories ->
                categoryAdapter.submitList(categories)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.isLoading.collectLatest { isLoading ->
                progressBar.isVisible = isLoading
                recyclerView.isVisible = !isLoading && categoryViewModel.error.value == null
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoryViewModel.error.collectLatest { error ->
                errorTextView.isVisible = error != null
                errorTextView.text = error
                recyclerView.isVisible = error == null && !categoryViewModel.isLoading.value
            }
        }
    }
}