package com.example.slickkwear

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.airbnb.lottie.LottieAnimationView
import com.example.pinkpanterwear.R
import com.example.slickkwear.Model.Categories
import com.example.slickkwear.Model.Products
import com.example.slickkwear.Model.SliderItem
import com.example.slickkwear.ViewHolder.HomeCategoryViewHolder
import com.example.slickkwear.ViewHolder.HomeDealsViewHolder
import com.example.slickkwear.ViewHolder.HomeProductsViewHolder
import com.example.slickkwear.ViewHolder.HomeTrendingViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso

class UserHomeFragment : Fragment() {
    private var sliderView: ViewPager2? = null
    private var adapter: ViewPager2SliderAdapter? = null
    private var sliderIndicator: TabLayout? = null
    private val sliderItems: MutableList<SliderItem> = ArrayList()

    private var productRef: FirebaseFirestore? = null
    private var query: Query? = null
    private var queryCategory: Query? = null
    private var queryDeals: Query? = null
    private var queryTrending: Query? = null
    private var queryProducts: Query? = null

    private var recyclerViewCategory: RecyclerView? = null
    private var recyclerViewDeals: RecyclerView? = null
    private var recyclerViewTrending: RecyclerView? = null
    private var recyclerViewProducts: RecyclerView? = null
    private var layoutManagerCategory: RecyclerView.LayoutManager? = null
    private var layoutManagerDeals: RecyclerView.LayoutManager? = null
    private var layoutManagerTrending: RecyclerView.LayoutManager? = null
    private var layoutManagerProducts: RecyclerView.LayoutManager? = null
    private var view: View? = null
    private var main_loading_bar: ConstraintLayout? = null

    private val loadingBar: ProgressBar? = null

    var lottieAnimationView: LottieAnimationView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_home, container, false)

        //        userSearch();

//        loadingBar = (ProgressBar) view.findViewById(R.id.)
//        getInstrumentation().waitForIdleSync()
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.VISIBLE

        lottieAnimationView = view!!.findViewById<LottieAnimationView>(R.id.lottie)
        sliderView = view!!.findViewById<ViewPager2>(R.id.imageSlider)
        sliderIndicator = view!!.findViewById<TabLayout>(R.id.imageSliderIndicator)

        productRef = FirebaseFirestore.getInstance()
        query = productRef!!.collection("Products")

        main_loading_bar = view!!.findViewById<ConstraintLayout>(R.id.main_loading_bar)

        homeSliderBanner()


        doStuffOnBackground()


        return view
    }

    private fun doStuffOnBackground() {
        queryCategory = productRef!!.collection("Categories")

        recyclerViewCategory = view!!.findViewById<RecyclerView>(R.id.recycler_view_category)
        recyclerViewCategory.setHasFixedSize(false)
        recyclerViewCategory.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerCategory = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerViewCategory.setLayoutManager(layoutManagerCategory)

        homeCategory()

        queryDeals =
            productRef!!.collection("Products").orderBy("ProductName", Query.Direction.ASCENDING)
                .limit(3)

        recyclerViewDeals = view!!.findViewById<RecyclerView>(R.id.recycler_view_today_deals)
        recyclerViewDeals.setHasFixedSize(false)
        recyclerViewDeals.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerDeals = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerViewDeals.setLayoutManager(layoutManagerDeals)

        homeDeals()


        queryTrending = productRef!!.collection("Products")

        recyclerViewTrending = view!!.findViewById<RecyclerView>(R.id.recycler_view_home_trending)
        recyclerViewTrending.setHasFixedSize(false)
        recyclerViewTrending.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerTrending = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerViewTrending.setLayoutManager(layoutManagerTrending)

        homeTrending()

        queryProducts = productRef!!.collection("Products")

        recyclerViewProducts = view!!.findViewById<RecyclerView>(R.id.recycler_view_home_products)
        recyclerViewProducts.setHasFixedSize(false)
        recyclerViewProducts.setNestedScrollingEnabled(false)
        //        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = GridLayoutManager(context, 2)
        recyclerViewProducts.setLayoutManager(layoutManagerProducts)

        homeProducts()
    }


    private fun homeSliderBanner() {
        adapter = ViewPager2SliderAdapter(requireContext(), sliderItems)
        sliderView!!.adapter = adapter

        query!!.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            sliderItems.clear()
            for (result in queryDocumentSnapshots) {
                val sliderItem = SliderItem()
                sliderItem.description = result.getString("ProductName")
                sliderItem.imageUrl = result.getString("ProductImage")
                sliderItems.add(sliderItem)
            }
            adapter!!.notifyDataSetChanged()
        }

        TabLayoutMediator(
            sliderIndicator!!, sliderView!!
        ) { tab: TabLayout.Tab?, position: Int -> }.attach()

        // Auto-scroll (opcional)
        sliderView!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private val handler = Handler(Looper.getMainLooper())
            private var runnable: Runnable? = null

            override fun onPageSelected(position: Int) {
                if (runnable != null) handler.removeCallbacks(runnable!!)
                runnable = Runnable {
                    val next = (position + 1) % adapter!!.itemCount
                    sliderView!!.setCurrentItem(next, true)
                }
                handler.postDelayed(runnable!!, 3000) // 3 segundos
            }
        })
    }


    //    @Override
    //    public void onStart() {
    //        super.onStart();
    //
    //        main_loading_bar.setVisibility(View.GONE);
    //        homeCategory();
    //
    //        homeDeals();
    /**/ */ //        homeTrending();
    //
    //        homeProducts();
    //    }
    private fun homeCategory() {
        val options = FirestoreRecyclerOptions.Builder<Categories>()
            .setQuery(queryCategory!!, Categories::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Categories, HomeCategoryViewHolder> =
            object : FirestoreRecyclerAdapter<Categories, HomeCategoryViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: HomeCategoryViewHolder,
                    position: Int,
                    model: Categories
                ) {
                    holder.txtCategoryName.text = model.categoryName
                    Picasso.get().load(model.categoryImage).into(holder.txtCategoryImage)

                    //                Onclick listener to take the user to the products details
                    holder.itemView.setOnClickListener {
                        val x = UserCategoryProductsFragment()
                        val args = Bundle()
                        args.putString("categoryUniqueID", model.categoryUniqueID)
                        x.arguments = args

                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, x)
                            .addToBackStack("") //To go back to previous fragment
                            .commit()

                        //                                Intent intent = new Intent(getContext(), UserCategoryFragment.class);
                        /**/                                intent.putExtra(
                        "CategoryUniqueID",
                        model.getCategoryUniqueID()
                    ); */
                        //                                startActivity(intent);
                    }

                    //                Onclick listener to take the user to the products details
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): HomeCategoryViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_home_category_layout, parent, false)
                    val holder = HomeCategoryViewHolder(view)
                    return holder
                }
            }

        recyclerViewCategory!!.adapter = adapter
        adapter.startListening()
        //        progressBar1.setVisibility(View.GONE);
    }


    private fun homeDeals() {
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(queryDeals!!, Products::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Products, HomeDealsViewHolder> =
            object : FirestoreRecyclerAdapter<Products, HomeDealsViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: HomeDealsViewHolder,
                    position: Int,
                    model: Products
                ) {
                    holder.txtDealName.text = model.productName

                    val initialPrice = "Ksh " + model.productPrice
                    //                String text2 = "<strike><font color=\'#757575\'>" + initialPrice + "</font></strike>";
                    /**/                textview.setText(Html.fromHtml(text)); */
                    holder.txtDealPriceInitial.text =
                        Html.fromHtml("<strike><font color='#757575'>$initialPrice</font></strike>")
                    holder.txtDealPriceDiscounted.text = "Ksh " + model.productPrice
                    Picasso.get().load(model.productImage).into(holder.txtDealImage)

                    //                Onclick listener to take the user to the products details
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            context,
                            AdminProductsDetailsActivity::class.java
                        )
                        intent.putExtra("ProductUniqueID", model.productUniqueID)
                        startActivity(intent)
                    }

                    //                Onclick listener to take the user to the products details
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): HomeDealsViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_home_deals_layout, parent, false)
                    val holder = HomeDealsViewHolder(view)
                    return holder
                }
            }
        recyclerViewDeals!!.adapter = adapter
        adapter.startListening()
    }

    private fun homeTrending() {
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(queryTrending!!, Products::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Products, HomeTrendingViewHolder> =
            object : FirestoreRecyclerAdapter<Products, HomeTrendingViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: HomeTrendingViewHolder,
                    position: Int,
                    model: Products
                ) {
                    holder.txtTrendingName.text = model.productName
                    holder.txtTrendingPrice.text = "Ksh " + model.productPrice
                    Picasso.get().load(model.productImage).into(holder.txtTrendingImage)

                    //                Onclick listener to take the user to the products details
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            context,
                            UserProductsDetailsActivity::class.java
                        )
                        intent.putExtra("ProductUniqueID", model.productUniqueID)
                        startActivity(intent)
                    }

                    //                Onclick listener to take the user to the products details
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): HomeTrendingViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_home_trending_layout, parent, false)
                    val holder = HomeTrendingViewHolder(view)
                    return holder
                }
            }

        recyclerViewTrending!!.adapter = adapter
        adapter.startListening()
        //        progressBar1.setVisibility(View.GONE);
    }


    private fun homeProducts() {
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(queryProducts!!, Products::class.java)
            .build()

        val adapter: FirestoreRecyclerAdapter<Products, HomeProductsViewHolder> =
            object : FirestoreRecyclerAdapter<Products, HomeProductsViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: HomeProductsViewHolder,
                    position: Int,
                    model: Products
                ) {
                    holder.txtProductsName.text = model.productName
                    holder.txtProductsPrice.text = "Ksh " + model.productPrice
                    Picasso.get().load(model.productImage).into(holder.txtProductsImage)

                    //                Onclick listener to take the user to the products details
                    holder.itemView.setOnClickListener {
                        val intent = Intent(
                            context,
                            UserProductsDetailsActivity::class.java
                        )
                        intent.putExtra("productUniqueID", model.productUniqueID)
                        startActivity(intent)
                    }

                    //                Onclick listener to take the user to the products details
                }

                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): HomeProductsViewHolder {
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_home_products_layout, parent, false)
                    val holder = HomeProductsViewHolder(view)
                    return holder
                }
            }

        recyclerViewProducts!!.adapter = adapter
        adapter.startListening()
        //        progressBar1.setVisibility(View.GONE);
    } //    public interface OnBackPressed {
    //        void onBackPressed();
    //    }
    //    public void onBackPressed() {
    //
    //    }
    //public boolean onBackPressed() {
    //    return false;
    //}
}