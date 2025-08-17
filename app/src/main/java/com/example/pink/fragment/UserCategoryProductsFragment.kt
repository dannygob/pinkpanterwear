package com.example.pink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.request.Glide
import com.example.pink.R
import com.example.pink.activities.UserProductsDetailsActivity
import com.example.pink.model.Products
import com.example.pink.viewHolder.UserCategoryProductsViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class UserCategoryProductsFragment : Fragment() {

    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var backToPrevBtn: TextView
    private lateinit var category_products_sort_btn: TextView
    private lateinit var category_products_filter_btn: TextView
    private lateinit var category_products_change_layout_btn: TextView
    private lateinit var category_products_most_popular_sort: TextView
    private lateinit var category_products_latest_sort: TextView
    private lateinit var category_products_lowest_price_sort: TextView
    private lateinit var category_products_highest_price_sort: TextView
    private lateinit var category_products_sort_layout: LinearLayout
    private lateinit var translucent_layout_sort: LinearLayout
    private lateinit var translucent_layout_filter: LinearLayout
    private lateinit var category_products_filter_layout: RelativeLayout
    private lateinit var category_products_null_products_layout: RelativeLayout
    private lateinit var category_products_filter_min: TextInputLayout
    private lateinit var category_products_filter_max: TextInputLayout
    private lateinit var category_products_filter_reset_btn: Button
    private lateinit var category_products_filter_apply_btn: Button
    private lateinit var lottieAnimationView: LottieAnimationView

    private val productRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var queryProducts: Query? = null
    private var categoryUniqueID: String? = null
    private var min_filter: String? = null
    private var max_filter: String? = null
    private var isGridLayout = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_user_category_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE

        lottieAnimationView = view.findViewById(R.id.category_products_empty_animation)
        backToPrevBtn = view.findViewById(R.id.search_view_back_to_prev_btn)
        backToPrevBtn.visibility = View.VISIBLE

        category_products_sort_layout = view.findViewById(R.id.category_products_sort_layout)
        category_products_sort_btn = view.findViewById(R.id.category_products_sort_btn)

        category_products_most_popular_sort =
            view.findViewById(R.id.category_products_most_popular_sort)
        category_products_latest_sort = view.findViewById(R.id.category_products_latest_sort)
        category_products_lowest_price_sort =
            view.findViewById(R.id.category_products_lowest_price_sort)
        category_products_highest_price_sort =
            view.findViewById(R.id.category_products_highest_price_sort)

        translucent_layout_sort = view.findViewById(R.id.translucent_layout_sort)

        category_products_filter_btn = view.findViewById(R.id.category_products_filter_btn)
        category_products_filter_layout = view.findViewById(R.id.category_products_filter_layout)
        translucent_layout_filter = view.findViewById(R.id.translucent_layout_filter)

        category_products_filter_min = view.findViewById(R.id.category_products_filter_min)
        category_products_filter_max = view.findViewById(R.id.category_products_filter_max)

        category_products_filter_reset_btn =
            view.findViewById(R.id.category_products_filter_reset_btn)
        category_products_filter_apply_btn =
            view.findViewById(R.id.category_products_filter_apply_btn)

        category_products_null_products_layout =
            view.findViewById(R.id.category_products_null_products_layout)
        category_products_change_layout_btn =
            view.findViewById(R.id.category_products_change_layout_btn)

        backToPrevBtn.setOnClickListener {
            activity?.onBackPressed()
        }

        categoryUniqueID = requireArguments().getString("category")
        queryProducts =
            productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.isNestedScrollingEnabled = false
        recyclerViewProducts.layoutManager = GridLayoutManager(context, 2)

        changeProductsLayout()
        sortProducts()
        filterProducts()
        productsRView()
    }

    private fun changeProductsLayout() {
        category_products_change_layout_btn.setOnClickListener {
            isGridLayout = !isGridLayout
            if (isGridLayout) {
                recyclerViewProducts.layoutManager = GridLayoutManager(context, 2)
                category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_list_24,
                    0
                )
            } else {
                recyclerViewProducts.layoutManager = LinearLayoutManager(context)
                category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_grid_24,
                    0
                )
            }
            productsRView()
        }
    }

    private fun filterProducts() {
        val slideInRight: Animation? = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
        val slideOutRight: Animation? =
            AnimationUtils.loadAnimation(context, R.anim.slide_out_right)

        category_products_filter_btn.setOnClickListener {
            if (category_products_filter_layout.isGone) {
                translucent_layout_filter.visibility = View.VISIBLE
                category_products_filter_layout.visibility = View.VISIBLE
                category_products_filter_layout.startAnimation(slideInRight)
            } else {
                category_products_filter_layout.startAnimation(slideOutRight)
                category_products_filter_layout.visibility = View.GONE
                translucent_layout_filter.visibility = View.GONE
            }
        }

        translucent_layout_filter.setOnClickListener {
            category_products_filter_layout.startAnimation(slideOutRight)
            category_products_filter_layout.visibility = View.GONE
            translucent_layout_filter.visibility = View.GONE
        }

        category_products_filter_apply_btn.setOnClickListener {
            min_filter = category_products_filter_min.editText?.text.toString()
            max_filter = category_products_filter_max.editText?.text.toString()

            if (min_filter.isNullOrEmpty()) {
                category_products_filter_min.error = "Cannot be empty!"
            } else if (max_filter.isNullOrEmpty()) {
                category_products_filter_max.error = "Cannot be empty!"
            } else if (min_filter!!.toInt() < 0) {
                category_products_filter_min.error = "Min value is 0 !"
            } else if (min_filter!!.toInt() > max_filter!!.toInt()) {
                category_products_filter_max.error = "Must be greater than min. value !"
            } else {
                category_products_filter_layout.startAnimation(slideOutRight)
                category_products_filter_layout.visibility = View.GONE
                translucent_layout_filter.visibility = View.GONE
                category_products_filter_min.error = null
                category_products_filter_max.error = null
                filterProductsMinMax()
            }
        }

        category_products_filter_reset_btn.setOnClickListener {
            category_products_filter_max.editText?.setText("")
            category_products_filter_min.editText?.setText("")
            category_products_filter_min.error = null
            category_products_filter_max.error = null
            category_products_filter_layout.startAnimation(slideOutRight)
            category_products_filter_layout.visibility = View.GONE
            translucent_layout_filter.visibility = View.GONE
            sortMostPopular()
        }
    }

    private fun filterProductsMinMax() {
        val new_min = min_filter!!.toInt()
        val new_max = max_filter!!.toInt()

        queryProducts = productRef.collection("Products")
            .whereGreaterThanOrEqualTo("ProductPrice", new_min)
            .whereLessThanOrEqualTo("ProductPrice", new_max)
            .orderBy("ProductPrice", Query.Direction.ASCENDING)

        queryProducts?.get()?.addOnSuccessListener { queryDocumentSnapshots ->
            category_products_null_products_layout.visibility =
                if (queryDocumentSnapshots.isEmpty) View.VISIBLE else View.GONE
        }

        productsRView()
    }

    private fun sortProducts() {
        val slideDown: Animation? = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        val slideUp: Animation? = AnimationUtils.loadAnimation(context, R.anim.slide_up)

        category_products_sort_btn.setOnClickListener {
            if (category_products_sort_layout.isGone) {
                translucent_layout_sort.visibility = View.VISIBLE
                category_products_sort_layout.visibility = View.VISIBLE
                category_products_sort_layout.startAnimation(slideUp)
                category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_up_24,
                    0
                )
            } else {
                category_products_sort_layout.startAnimation(slideDown)
                category_products_sort_layout.visibility = View.GONE
                translucent_layout_sort.visibility = View.GONE
                category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_down_24,
                    0
                )
            }
        }

        translucent_layout_sort.setOnClickListener {
            category_products_sort_layout.startAnimation(slideDown)
            category_products_sort_layout.visibility = View.GONE
            translucent_layout_sort.visibility = View.GONE
            category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
        }

        val sortClickListener = View.OnClickListener { v ->
            resetSortDrawables()
            val selectedTextView = v as TextView
            selectedTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_check_24,
                0
            )
            category_products_sort_btn.text = selectedTextView.text

            when (v.id) {
                R.id.category_products_most_popular_sort -> sortMostPopular()
                R.id.category_products_latest_sort -> sortLatest()
                R.id.category_products_lowest_price_sort -> sortLowestPrice()
                R.id.category_products_highest_price_sort -> sortHighestPrice()
            }

            category_products_sort_layout.startAnimation(slideDown)
            category_products_sort_layout.visibility = View.GONE
            translucent_layout_sort.visibility = View.GONE
            category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
        }

        category_products_most_popular_sort.setOnClickListener(sortClickListener)
        category_products_latest_sort.setOnClickListener(sortClickListener)
        category_products_lowest_price_sort.setOnClickListener(sortClickListener)
        category_products_highest_price_sort.setOnClickListener(sortClickListener)
    }

    private fun resetSortDrawables() {
        category_products_most_popular_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            0
        )
        category_products_latest_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        category_products_lowest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            0
        )
        category_products_highest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0,
            0,
            0,
            0
        )
    }

    private fun sortMostPopular() {
        queryProducts =
            productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)
        productsRView()
    }

    private fun sortLatest() {
        queryProducts =
            productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)
                .orderBy("DateCreated", Query.Direction.DESCENDING)
        productsRView()
    }

    private fun sortLowestPrice() {
        queryProducts = productRef.collection("Products")
            .whereEqualTo("ProductCategory", categoryUniqueID)
            .orderBy("ProductPrice", Query.Direction.ASCENDING)
        productsRView()
    }

    private fun sortHighestPrice() {
        queryProducts = productRef.collection("Products")
            .whereEqualTo("ProductCategory", categoryUniqueID)
            .orderBy("ProductPrice", Query.Direction.DESCENDING)
        productsRView()
    }

    private fun productsRView() {
        val config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        )

        val options = FirestorePagingOptions.Builder<Products>()
            .setQuery(queryProducts!!, config, Products::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter =
            object : FirestorePagingAdapter<Products, UserCategoryProductsViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: UserCategoryProductsViewHolder,
                    position: Int,
                    model: Products,
                ) {
                    var productName: String = model.productName
                    if (productName.length > 20) {
                        productName = productName.substring(0, 19) + "..."
                    }

                    holder.txtCategoryProductsName?.text = productName
                    holder.txtCategoryProductsPrice?.text =
                        "${getString(R.string.currency)} ${model.productPrice}"
                    Glide.with(holder.itemView.context).load(model.productImage)
                        .into(holder.txtCategoryProductsImage)

                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, UserProductsDetailsActivity::class.java)
                        intent.putExtra("ProductUniqueID", model.getProductUniqueID())
                        startActivity(intent)
                    }
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int,
                ): UserCategoryProductsViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_category_products_layout, parent, false)
                    return UserCategoryProductsViewHolder(view)
                }

                override fun onLoadingStateChanged(state: LoadingState) {
                    when (state) {
                        LoadingState.LOADING_INITIAL -> { /* Show loading indicator */
                        }

                        LoadingState.LOADING_MORE -> { /* Show loading indicator */
                        }

                        LoadingState.LOADED -> { /* Hide loading indicator */
                        }

                        LoadingState.FINISHED -> { /* Hide loading indicator */
                        }

                        LoadingState.ERROR -> {
                            retry()
                        }
                    }
                }
            }

        recyclerViewProducts.adapter = adapter
        adapter.startListening()
    }
}
