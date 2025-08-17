package com.example.pink.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pink.model.Categories
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreCategoryPagingSource(
    private val baseQuery: Query,
) : PagingSource<DocumentSnapshot, Categories>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Categories> {
        return try {
            val currentQuery = if (params.key != null) {
                baseQuery.startAfter(params.key)
            } else {
                baseQuery
            }.limit(params.loadSize.toLong())

            val snapshot = currentQuery.get().await()
            val documents = snapshot.documents

            val data = documents.mapNotNull { it.toObject(Categories::class.java) }
            val lastDocument = documents.lastOrNull()

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = lastDocument
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Categories>): DocumentSnapshot? {
        return null
    }
}
