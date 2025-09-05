package com.example.pink.util

import com.example.pink.model.Products
import com.example.pink.model.api.FakeStoreProduct
import com.example.pink.model.api.PlatziProduct

// 🔄 Mapeo desde PlatziProduct a Products
fun PlatziProduct.toRoomProduct(): Products {
    return Products(
        productUniqueID = this.id.toString(),
        productName = this.title ?: "Sin nombre",
        productDescription = this.description ?: "Sin descripción",
        productImage = this.images?.firstOrNull() ?: "https://via.placeholder.com/150",
        productPrice = this.price ?: 0.0,
        productCategory = this.category?.name ?: "Sin categoría",
        productStatus = "imported",
        productDeleted = "false",
        dateCreated = "2025-09-05",
        timeCreated = "23:42"
    )
}

// 🔄 Mapeo desde FakeStoreProduct a Products
fun FakeStoreProduct.toRoomProduct(): Products {
    return Products(
        productUniqueID = this.id.toString(),
        productName = this.title ?: "Sin nombre",
        productDescription = this.description ?: "Sin descripción",
        productImage = this.image ?: "https://via.placeholder.com/150",
        productPrice = this.price ?: 0.0,
        productCategory = this.category ?: "Sin categoría",
        productStatus = "imported",
        productDeleted = "false",
        dateCreated = "2025-09-05",
        timeCreated = "23:42"
    )
}