package com.example.pinkpanterwear.network

import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") productId: Int): ProductResponse?

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/category/{categoryName}")
    suspend fun getProductsByCategory(@Path("categoryName") categoryName: String): List<ProductResponse>
}
