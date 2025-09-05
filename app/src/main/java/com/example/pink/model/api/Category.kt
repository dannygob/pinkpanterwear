package com.example.pink.model.api

data class Category(
    val id: Int? = null,         // Solo Platzi usa ID
    val name: String,
    val image: String? = null,    // Solo Platzi tiene imagen
)