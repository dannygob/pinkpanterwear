package com.example.pink.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pink.data.FirestoreCategoryPagingSource
import com.example.pink.model.Categories
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

class AdminCategoriesViewModel : ViewModel() {

    private val query: Query = FirebaseFirestore.getInstance()
        .collection("Categories")
        .orderBy("categoryName") // orden por el campo que tengas

    val categoriesFlow: Flow<PagingData<Categories>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { FirestoreCategoryPagingSource(query) }
        ).flow.cachedIn(viewModelScope)
}
