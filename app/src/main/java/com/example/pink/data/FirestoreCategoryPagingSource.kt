package com.example.pink.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pink.model.Categories
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreCategoryPagingSource(
    private val query: Query,
) : PagingSource<Query, Categories>() {

    override suspend fun load(params: LoadParams<Query>): LoadResult<Query, Categories> {
        return try {
            val currentQuery = if (params.key != null) {
                params.key!!.startAfterDocument(params.key!!.get().await().documents.last())
            } else {
                query
            }.limit(params.loadSize.toLong())

            val snapshot = currentQuery.get().await()

            val data = snapshot.documents.mapNotNull { it.toObject(Categories::class.java) }

            val nextKey = if (snapshot.documents.isNotEmpty()) currentQuery else null

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Query, Categories>): Query? = null
}
