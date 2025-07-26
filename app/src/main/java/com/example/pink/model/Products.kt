package com.example.pink.model

data class Products(
    val productUniqueID: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productImage: String = "",
    val productCategory: String = "",
    val productStatus: String = "",
    val dateCreated: String = "",
    val timeCreated: String = "",
    val productPrice: Int = 0,
)