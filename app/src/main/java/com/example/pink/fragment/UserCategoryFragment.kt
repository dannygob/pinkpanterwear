package com.example.pink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.R
import com.example.pink.activities.UserCategoryProductsActivity
import com.example.pink.adapter.CategoryPagingAdapter
import com.example.pink.viewModel.UserCategoryViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserCategoryFragment : Fragment() {

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val viewModel: UserCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_category, container, false)

        recyclerViewCategory = view.findViewById(R.id.recycler_view_user_category)
        progressBar = view.findViewById(R.id.progress_bar_category)

        recyclerViewCategory.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(context, 2)
        }

        setupAdapter()

        return view
    }

    private fun setupAdapter() {
        val adapter = CategoryPagingAdapter { category ->
            // Aquí puedes abrir un fragment o actividad según tu flujo
            // Por ejemplo:
            val intent = Intent(requireContext(), UserCategoryProductsActivity::class.java)
            intent.putExtra("category", category.categoryName)
            startActivity(intent)
        }

        recyclerViewCategory.adapter = adapter

        lifecycleScope.launch {
            viewModel.categoryFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            progressBar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            if (loadState.refresh is LoadState.Error) {
                // Puedes mostrar un mensaje de error
                // Toast.makeText(context, "Error cargando categorías", Toast.LENGTH_SHORT).show()
                adapter.retry()
            }
        }
    }
}
