package com.example.pink.repositories

import com.example.pink.model.Categories
import com.example.pink.model.Products
import com.example.pink.model.api.FakeStoreProduct
import com.example.pink.model.api.PlatziProduct
import com.example.pink.network.FakeStoreApi
import com.example.pink.network.PlatziApi
import com.example.pink.util.toRoomProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(
    private val platziApi: PlatziApi,
    private val fakeStoreApi: FakeStoreApi,
) {

    suspend fun fetchAllProducts(): List<Products> = withContext(Dispatchers.IO) {
        val platziResponse = platziApi.getProducts()
        val fakeStoreResponse = fakeStoreApi.getProducts()

        val platziProducts = platziResponse.body()?.map(PlatziProduct::toRoomProduct) ?: emptyList()
        val fakeStoreProducts =
            fakeStoreResponse.body()?.map(FakeStoreProduct::toRoomProduct) ?: emptyList()

        return@withContext platziProducts + fakeStoreProducts
    }

    suspend fun fetchCategories(): List<Categories> = withContext(Dispatchers.IO) {
        val platziResponse = platziApi.getCategories()
        val fakeStoreResponse = fakeStoreApi.getCategoryNames()

        val platziCategories = platziResponse.body()?.map {
            Categories(
                categoryUniqueID = it.id.toString(),
                categoryName = it.name,
                categoryImage = it.image ?: "",
                categoryStatus = "imported",
                categoryDeleted = "false",
                dateCreated = "2025-09-05",
                timeCreated = "23:42"
            )
        } ?: emptyList()

        val fakeCategories = fakeStoreResponse.body()?.map {
            Categories(
                categoryUniqueID = it,
                categoryName = it,
                categoryImage = "https://via.placeholder.com/150",
                categoryStatus = "imported",
                categoryDeleted = "false",
                dateCreated = "2025-09-05",
                timeCreated = "23:42"
            )
        } ?: emptyList()

        return@withContext platziCategories + fakeCategories
    }
}