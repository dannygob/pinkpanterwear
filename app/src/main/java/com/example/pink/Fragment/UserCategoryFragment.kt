package com.example.pink.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pink.Activities.AdminProductsDetailsActivity
import com.example.pink.Model.Categories
import com.example.pink.R
import com.example.pink.ViewHolder.CategoryCategoryViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class UserCategoryFragment : Fragment() {

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var layoutManagerCategories: RecyclerView.LayoutManager
    private lateinit var progressBar: ProgressBar
    private lateinit var categoryRef: FirebaseFirestore
    private lateinit var queryCategory: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_category, container, false)

        categoryRef = FirebaseFirestore.getInstance()
        queryCategory = categoryRef.collection("Categories")

        recyclerViewCategory = view.findViewById(R.id.recycler_view_user_category)
        recyclerViewCategory.setHasFixedSize(true)
        recyclerViewCategory.isNestedScrollingEnabled = false
        layoutManagerCategories = GridLayoutManager(context, 2)
        recyclerViewCategory.layoutManager = layoutManagerCategories

        progressBar = view.findViewById(R.id.progress_bar_category)

        return view
    }

    override fun onStart() {
        super.onStart()

        val config = PagingConfig(
            pageSize = 5,
            prefetchDistance = 10,
            enablePlaceholders = true,
            initialLoadSize = 10,
            maxSize = 20
        )

        val options = FirestorePagingOptions.Builder<Categories>()
            .setQuery(queryCategory, config, Categories::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter =
            object : FirestorePagingAdapter<Categories, CategoryCategoryViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): CategoryCategoryViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_category_category_layout, parent, false)
                    return CategoryCategoryViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: CategoryCategoryViewHolder,
                    position: Int,
                    model: Categories,
                ) {
                    holder.txtCategoryName.text = model.categoryName
                    Picasso.get().load(model.categoryImage).into(holder.txtCategoryImage)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, AdminProductsDetailsActivity::class.java)
                        intent.putExtra("ProductUniqueID", model.categoryUniqueID)
                        startActivity(intent)
                    }
                }

                override fun onLoadingStateChanged(state: LoadingState) {
                    when (state) {
                        LoadingState.LOADING_INITIAL,
                        LoadingState.LOADING_MORE,
                            -> progressBar.visibility = View.VISIBLE

                        LoadingState.LOADED,
                        LoadingState.FINISHED,
                            -> progressBar.visibility = View.GONE

                        LoadingState.ERROR -> {
                            retry()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }

        recyclerViewCategory.adapter = adapter
        adapter.startListening()
    }
}
