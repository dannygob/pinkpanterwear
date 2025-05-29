package com.example.pinkpanterwear.data.api

import com.example.pinkpanterwear.data.Product // Assuming your Product data class is here
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product // Assuming product ID is an Int based on typical APIs
}