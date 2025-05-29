package com.example.pinkpanterwear.ui

import android.os.Bundle
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager // or GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinkpanterwear.R
import com.example.pinkpanterwear.data.Product
import com.example.pinkpanterwear.data.Category

class UserCategoryProductsFragment : Fragment() {

    private lateinit var productsRecyclerView: RecyclerView
    private var categoryId: String? = null // Or Int, depending on your category ID type

 companion object {
 private const val PRODUCT_ID_EXTRA = "PRODUCT_ID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_category_products, container, false)

        productsRecyclerView = view.findViewById(R.id.products_recycler_view)

        // Set the layout manager for the RecyclerView
        // productsRecyclerView.layoutManager = LinearLayoutManager(context)
        productsRecyclerView.layoutManager = LinearLayoutManager(context) // Use LinearLayoutManager
        // Retrieve the categoryId from arguments
        categoryId = arguments?.getString(ARG_CATEGORY_ID)

 val productList: List<Product>
        if (categoryId != null) {
            productList = loadProducts(categoryId!!)
        } else {
            // Handle the case where categoryId is null (e.g., show an error message)
 // For now, provide an empty list or show a message
 productList = emptyList()
        }

 productsRecyclerView.adapter = ProductAdapter(productList) { product ->
 // Handle product item click here
 val intent = Intent(context, UserProductDetailsActivity::class.java).apply {
 putExtra(PRODUCT_ID_EXTRA, product.id)
 }
 startActivity(intent)
 }

        return view
    }

    private fun loadProducts(categoryId: String): List<Product> {
        // Hardcoded list of sample products
 val allProducts = listOf(
 Product("p1", "Product 1", 10.0, "image_url_1", "cat1"),
 Product("p2", "Product 2", 20.0, "image_url_2", "cat2"),
 Product("p3", "Product 3", 30.0, "image_url_3", "cat1"),
 Product("p4", "Product 4", 40.0, "image_url_4", "cat3"),
 Product("p5", "Product 5", 50.0, "image_url_5", "cat2")
 )

        // Filter products by categoryId
        return allProducts.filter { it.categoryId == categoryId }
    }

        private const val ARG_CATEGORY_ID = "categoryId" // Define a key for the argument

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param categoryId Parameter 1.
         * @return A new instance of fragment UserCategoryProductsFragment.
         */
        @JvmStatic
        fun newInstance(categoryId: String) = // Change String to Int if using Int IDs
            UserCategoryProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId) // Change putString to putInt if using Int IDs
                }
            }
    }
}