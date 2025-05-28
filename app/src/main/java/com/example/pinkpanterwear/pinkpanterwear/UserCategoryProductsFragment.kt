package com.example.slickkwear

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.pinkpanterwear.R
import com.example.slickkwear.Model.Products
import com.example.slickkwear.ViewHolder.UserCategoryProductsViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class UserCategoryProductsFragment : Fragment() {
    private var view: View? = null
    private var productRef: FirebaseFirestore? = null
    private var queryProducts: Query? = null
    private var recyclerViewProducts: RecyclerView? = null
    private var layoutManagerProducts: RecyclerView.LayoutManager? = null
    private var categoryUniqueID: String? = null
    private var min_filter: String? = null
    private var max_filter: String? = null
    private var backToPrevBtn: TextView? = null
    private var category_products_sort_btn: TextView? = null
    private var category_products_filter_btn: TextView? = null
    private val category_products_change_layout_btn: TextView? = null
    private var category_products_most_popular_sort: TextView? = null
    private var category_products_latest_sort: TextView? = null
    private var category_products_lowest_price_sort: TextView? = null
    private var category_products_best_rating_sort: TextView? = null
    private var category_products_highest_price_sort: TextView? = null
    private var category_products_sort_layout: LinearLayout? = null
    private var translucent_layout_sort: LinearLayout? = null
    private var translucent_layout_filter: LinearLayout? = null
    private var category_products_filter_layout: RelativeLayout? = null
    private var category_products_null_products_layout: RelativeLayout? = null
    private var category_products_filter_min: TextInputLayout? = null
    private var category_products_filter_max: TextInputLayout? = null
    private var category_products_filter_reset_btn: Button? = null
    private var category_products_filter_apply_btn: Button? = null
    private var count = 0
    private var lottieAnimationView: LottieAnimationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_user_category_products, container, false)

        lottieAnimationView =
            view!!.findViewById<LottieAnimationView>(R.id.category_products_empty_animation)

        //        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragment_container2);
//        frameLayout.setVisibility(View.VISIBLE);
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE

        backToPrevBtn = view!!.findViewById<TextView>(R.id.search_view_back_to_prev_btn)
        backToPrevBtn.setVisibility(View.VISIBLE)

        category_products_sort_layout =
            view!!.findViewById<LinearLayout>(R.id.category_products_sort_layout)
        category_products_sort_btn = view!!.findViewById<TextView>(R.id.category_products_sort_btn)

        category_products_most_popular_sort =
            view!!.findViewById<TextView>(R.id.category_products_most_popular_sort)
        category_products_latest_sort =
            view!!.findViewById<TextView>(R.id.category_products_latest_sort)
        category_products_best_rating_sort =
            view!!.findViewById<TextView>(R.id.category_products_best_rating_sort)
        category_products_lowest_price_sort =
            view!!.findViewById<TextView>(R.id.category_products_lowest_price_sort)
        category_products_highest_price_sort =
            view!!.findViewById<TextView>(R.id.category_products_highest_price_sort)

        translucent_layout_sort = view!!.findViewById<LinearLayout>(R.id.translucent_layout_sort)

        category_products_filter_btn =
            view!!.findViewById<TextView>(R.id.category_products_filter_btn)
        category_products_filter_layout =
            view!!.findViewById<RelativeLayout>(R.id.category_products_filter_layout)
        translucent_layout_filter =
            view!!.findViewById<LinearLayout>(R.id.translucent_layout_filter)

        category_products_filter_min =
            view!!.findViewById<TextInputLayout>(R.id.category_products_filter_min)
        category_products_filter_max =
            view!!.findViewById<TextInputLayout>(R.id.category_products_filter_max)

        category_products_filter_reset_btn =
            view!!.findViewById<Button>(R.id.category_products_filter_reset_btn)
        category_products_filter_apply_btn =
            view!!.findViewById<Button>(R.id.category_products_filter_apply_btn)


        backToPrevBtn.setOnClickListener(
            View.OnClickListener { // RETURN TO PREVIOUS FRAGMENT
                activity!!.onBackPressed()
            }
        )
        category_products_null_products_layout =
            view!!.findViewById<RelativeLayout>(R.id.category_products_null_products_layout)

        //        category_products_change_layout_btn = (TextView) view.findViewById(R.id.category_products_change_layout_btn);

//        changeProductsLayout();
        sortProducts()

        filterProducts()

        categoryUniqueID = arguments!!.getString("categoryUniqueID")


        productRef = FirebaseFirestore.getInstance()
        queryProducts =
            productRef!!.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        productsRView()

        return view
    }

    //CHANGE PRODUCT LAYOUT BTN GRID AND LINEAR
    //    private void changeProductsLayout() {
    //        count = 0;
    //        category_products_change_layout_btn.setOnClickListener(
    //                new View.OnClickListener() {
    //                    @Override
    //                    public void onClick(View view) {
    //
    //                        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID);
    //
    /**/                        if(count == 0)
    {
        */ //                            recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        //                            recyclerViewProducts.setHasFixedSize(false);
        //                            recyclerViewProducts.setNestedScrollingEnabled(false);
        //                            layoutManagerProducts = new LinearLayoutManager(getContext());
        //                            category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_list_24, 0);
        //                            recyclerViewProducts.setLayoutManager(layoutManagerProducts);
        /**/                            count++
        * /
    }
    * /                        else
    {
        * /                            recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview)
        * /                            recyclerViewProducts.setHasFixedSize(false)
        * /                            recyclerViewProducts.setNestedScrollingEnabled(false)
        * /                            layoutManagerProducts = new GridLayoutManager(getContext(), 2)
        * /                            category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_grid_24, 0)
        * /                            recyclerViewProducts.setLayoutManager(layoutManagerProducts)
        * /                            count--
        * /
    } */
    //
    //                        productsRView();
    //
    //                    }
    //                }
    //        );
    //    }
    private fun filterProducts() {
        val slide_in_right = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_in_right
        )

        val slide_out_right = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_out_right
        )

        // Start animation
        // SHOW FILTER LAYOUT
        count = 0
        category_products_filter_btn!!.setOnClickListener {
            if (count == 0) {
                category_products_filter_layout!!.startAnimation(slide_out_right)
                translucent_layout_filter!!.visibility = View.VISIBLE
                category_products_filter_layout!!.visibility = View.VISIBLE
                category_products_filter_layout!!.startAnimation(slide_in_right)
                count += 1
            } else {
                category_products_filter_layout!!.startAnimation(slide_out_right)
                category_products_filter_layout!!.visibility = View.GONE
                count -= 1
            }
        }

        //DISMISS FILTER LAYOUT
        translucent_layout_filter!!.setOnClickListener {
            category_products_filter_layout!!.startAnimation(slide_out_right)
            category_products_filter_layout!!.visibility = View.GONE
            count -= 1
            translucent_layout_filter!!.visibility = View.GONE
        }

        category_products_filter_apply_btn!!.setOnClickListener {
            min_filter = category_products_filter_min!!.editText!!.text.toString()
            max_filter = category_products_filter_max!!.editText!!.text.toString()
            if (min_filter!!.isEmpty()) {
                category_products_filter_min!!.error = "Cannot be empty!"
            } else if (max_filter!!.isEmpty()) {
                category_products_filter_max!!.error = "Cannot be empty!"
            } else if (min_filter!!.toInt() < 0) {
                category_products_filter_min!!.error = "Min value is 0 !"
            } else if (min_filter!!.toInt() > max_filter!!.toInt()) {
                category_products_filter_max!!.error = "Must be greater than min. value !"
            } else {
                category_products_filter_layout!!.startAnimation(slide_out_right)
                category_products_filter_layout!!.visibility = View.GONE
                count -= 1

                translucent_layout_filter!!.visibility = View.GONE

                category_products_filter_min!!.error = null
                category_products_filter_max!!.error = null

                filterProductsMinMax()
            }
        }

        category_products_filter_reset_btn!!.setOnClickListener {
            category_products_filter_max!!.editText!!.setText("")
            category_products_filter_min!!.editText!!.setText("")
            category_products_filter_min!!.error = null
            category_products_filter_max!!.error = null

            category_products_filter_layout!!.startAnimation(slide_out_right)
            category_products_filter_layout!!.visibility = View.GONE
            count -= 1

            translucent_layout_filter!!.visibility = View.GONE
            sortMostPopular()
        }
    }

    // FILTER PRODUCTS BASED ON PRICE
    private fun filterProductsMinMax() {
        val new_min = min_filter!!.toInt()
        val new_max = max_filter!!.toInt()

        queryProducts = productRef!!.collection("Products").whereGreaterThanOrEqualTo
        ("ProductPrice", new_min)
        .whereLessThanOrEqualTo("ProductPrice", new_max)
            .orderBy("ProductPrice", Query.Direction.ASCENDING)

        //Check if empty products result
        queryProducts!!.get().addOnSuccessListener { queryDocumentSnapshots ->
            if (queryDocumentSnapshots.isEmpty) {
                category_products_null_products_layout!!.visibility = View.VISIBLE
            } else {
                category_products_null_products_layout!!.visibility = View.GONE
            }
        }


        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        productsRView()
    }

    private fun sortProducts() {
        val slide_down = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_down
        )

        val slide_up = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_up
        )

        category_products_sort_layout!!.startAnimation(slide_down)

        //        translucent_layout_sort.setVisibility(View.GONE);

// Start animation
        // SHOW SORT LAYOUT
        count = 0
        category_products_sort_btn!!.setOnClickListener {
            if (count == 0) {
                translucent_layout_sort!!.visibility = View.VISIBLE
                category_products_sort_layout!!.visibility = View.VISIBLE
                category_products_sort_layout!!.startAnimation(slide_up)
                category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_up_24,
                    0
                )
                count += 1
            } else {
                category_products_sort_layout!!.startAnimation(slide_down)
                category_products_sort_layout!!.visibility = View.GONE
                category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_down_24,
                    0
                )
                count -= 1
            }
        }

        //DISMISS SORT LAYOUT
        translucent_layout_sort!!.setOnClickListener {
            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            count -= 1
            translucent_layout_sort!!.visibility = View.GONE
        }

        category_products_most_popular_sort!!.setOnClickListener {
            category_products_most_popular_sort
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_24,
                    0
                )
            category_products_latest_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_best_rating_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_lowest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_highest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )

            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            category_products_sort_btn!!.text = "Most Popular"

            count -= 1

            translucent_layout_sort!!.visibility = View.GONE
            sortMostPopular()
        }

        category_products_latest_sort!!.setOnClickListener {
            category_products_latest_sort
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_24,
                    0
                )
            category_products_most_popular_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_best_rating_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_lowest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_highest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )

            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            category_products_sort_btn!!.text = "Latest"
            count -= 1

            translucent_layout_sort!!.visibility = View.GONE
            sortLatest()
        }

        category_products_best_rating_sort!!.setOnClickListener {
            category_products_best_rating_sort
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_24,
                    0
                )
            category_products_latest_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_most_popular_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_lowest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_highest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )

            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            category_products_sort_btn!!.text = "Best Rating"
            count -= 1

            translucent_layout_sort!!.visibility = View.GONE
            sortBestRating()
        }

        category_products_lowest_price_sort!!.setOnClickListener {
            category_products_lowest_price_sort
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_24,
                    0
                )
            category_products_latest_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_best_rating_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_most_popular_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_highest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )

            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            category_products_sort_btn!!.text = "Lowest Price"
            count -= 1

            translucent_layout_sort!!.visibility = View.GONE
            sortLowestPrice()
        }

        category_products_highest_price_sort!!.setOnClickListener {
            category_products_highest_price_sort
                .setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_24,
                    0
                )
            category_products_latest_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_best_rating_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_lowest_price_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
            category_products_most_popular_sort!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )

            category_products_sort_layout!!.startAnimation(slide_down)
            category_products_sort_layout!!.visibility = View.GONE
            category_products_sort_btn!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_baseline_keyboard_arrow_down_24,
                0
            )
            category_products_sort_btn!!.text = "Highest Price"
            count -= 1

            translucent_layout_sort!!.visibility = View.GONE
            sortHighestPrice()
        }
    }

    private fun sortMostPopular() {
        queryProducts =
            productRef!!.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)


        productsRView()
    }


    private fun sortLatest() {
        queryProducts =
            productRef!!.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)
                .orderBy("DateCreated", Query.Direction.DESCENDING)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)


        productsRView()
    }

    private fun sortBestRating() {
        queryProducts =
            productRef!!.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        productsRView()
    }

    private fun sortLowestPrice() {
        queryProducts = productRef!!.collection("Products")
            .whereEqualTo("ProductCategory", categoryUniqueID)
            .orderBy("ProductPrice", Query.Direction.ASCENDING)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        productsRView()
    }

    private fun sortHighestPrice() {
        queryProducts = productRef!!.collection("Products")
            .whereEqualTo("ProductCategory", categoryUniqueID)
            .orderBy("ProductPrice", Query.Direction.DESCENDING)

        recyclerViewProducts =
            view!!.findViewById<RecyclerView>(R.id.category_products_recyclerview)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        productsRView()
    }


    private fun productsRView() {
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(queryProducts!!, Products::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Products, UserCategoryProductsViewHolder> =
            object : FirestoreRecyclerAdapter<Products, UserCategoryProductsViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): UserCategoryProductsViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_category_products_layout, parent, false)
                    return UserCategoryProductsViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: UserCategoryProductsViewHolder,
                    position: Int,
                    model: Products
                ) {
                    var product_name = model.productName
                    if (product_name!!.length > 20) {
                        product_name = product_name.substring(0, 19) + "..."
                    }
                    holder.txtCategoryProductsName.text = product_name
                    holder.txtCategoryProductsPrice.text = "Ksh " + model.productPrice
                    Picasso.get().load(model.productImage).into(holder.txtCategoryProductsImage)
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            context,
                            UserProductsDetailsActivity::class.java
                        )
                        intent.putExtra("ProductUniqueID", model.productUniqueID)
                        startActivity(intent)
                    }
                }
            }
        recyclerViewProducts!!.adapter = adapter
        adapter.startListening()
    }
}