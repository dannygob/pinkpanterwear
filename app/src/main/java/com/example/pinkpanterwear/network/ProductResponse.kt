package com.example.pinkpanterwear.network

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val id: Int,
    @SerializedName("title")
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    @SerializedName("image")
    val image: String,
    val rating: RatingResponse
)

data class RatingResponse(
    val rate: Double,
    val count: Int
)
