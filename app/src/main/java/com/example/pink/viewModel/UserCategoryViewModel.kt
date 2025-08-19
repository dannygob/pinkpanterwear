package com.example.pink.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pink.model.Categories
import com.example.pink.util.CategoryPagingSource
import kotlinx.coroutines.flow.Flow

class UserCategoryViewModel : ViewModel() {

    val categoryFlow: Flow<PagingData<Categories>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CategoryPagingSource() }
    ).flow.cachedIn(viewModelScope)
}
