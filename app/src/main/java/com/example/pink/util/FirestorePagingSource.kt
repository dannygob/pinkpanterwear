package com.example.pink.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pink.model.Categories
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestorePagingSource(
    private val query: Query,
) : PagingSource<DocumentSnapshot, Categories>() {

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Categories>): DocumentSnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Categories> {
        return try {
            val currentPage = if (params.key == null) {
                query.limit(params.loadSize.toLong()).get().await()
            } else {
                query.startAfter(params.key).limit(params.loadSize.toLong()).get().await()
            }

            val lastVisible = currentPage.documents.lastOrNull()

            val items = currentPage.toObjects(Categories::class.java)

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = lastVisible
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
