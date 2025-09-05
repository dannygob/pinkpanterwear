package com.example.pink.network

import com.example.pink.model.api.PlatziCategory
import com.example.pink.model.api.PlatziProduct
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PlatziApi {

    @GET("products")
    suspend fun getProducts(): Response<List<PlatziProduct>>

    @GET("categories")
    suspend fun getCategories(): Response<List<PlatziCategory>>

    companion object {
        fun create(): PlatziApi {
            return Retrofit.Builder()
                .baseUrl("https://api.escuelajs.co/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlatziApi::class.java)
        }
    }
}