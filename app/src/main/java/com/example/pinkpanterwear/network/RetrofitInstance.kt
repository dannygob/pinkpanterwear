package com.example.pinkpanterwear.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // Assuming Gson is used

object RetrofitInstance {

    private const val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // TODO: Ensure Gson dependency is in build.gradle
            .build()
    }

    val api: FakeStoreApiService by lazy {
        retrofit.create(FakeStoreApiService::class.java)
    }
}
