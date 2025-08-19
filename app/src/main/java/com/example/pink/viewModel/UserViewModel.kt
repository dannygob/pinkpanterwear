package com.example.pink.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.pink.util.UserPagingSource

class UserViewModel : ViewModel() {
    val userFlow = Pager(PagingConfig(pageSize = 20)) {
        UserPagingSource()
    }.flow.cachedIn(viewModelScope)
}