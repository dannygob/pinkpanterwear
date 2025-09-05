package com.example.pink.network

import com.example.pink.model.api.FakeStoreProduct
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FakeStoreApi {

    @GET("products")
    suspend fun getProducts(): Response<List<FakeStoreProduct>>

    @GET("products/categories")
    suspend fun getCategoryNames(): Response<List<String>>

    companion object {
        fun create(): FakeStoreApi {
            return Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FakeStoreApi::class.java)
        }
    }
}