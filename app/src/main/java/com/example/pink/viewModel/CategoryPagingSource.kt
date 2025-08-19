package com.example.pink.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pink.model.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CategoryPagingSource : PagingSource<Query, Categories>() {

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection("Categories")
    private val pageSize = 10

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, Categories> {
        return try {
            // Si params.key no está definido, usar la consulta base
            val currentQuery = params.key.takeIf { it != null } ?: collectionRef
                .orderBy("categoryName")
                .limit(pageSize.toLong())

            val snapshot: QuerySnapshot = currentQuery.get().await()
            val categories = snapshot.toObjects(Categories::class.java)

            val lastVisible = snapshot.documents.lastOrNull()

            val nextKey = lastVisible?.let {
                collectionRef.orderBy("categoryName")
                    .startAfter(it)
                    .limit(pageSize.toLong())
            }

            LoadResult.Page(
                data = categories,
                prevKey = null,
                nextKey = if (categories.isEmpty()) null else nextKey!!
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, Categories>): Query? {
        return null
    }
}
