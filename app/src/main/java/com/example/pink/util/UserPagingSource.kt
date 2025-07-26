package com.example.pink.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pink.model.User

class UserPagingSource : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        val users = (1..pageSize).map {
            User(id = (page - 1) * pageSize + it, name = "Usuario $it")
        }

        return LoadResult.Page(
            data = users,
            prevKey = if (page == 1) null else page - 1,
            nextKey = page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}