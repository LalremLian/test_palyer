package com.example.testapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.data.Resource
import com.example.data.repository.AppRepository
import com.example.network.model.MoviesResponse
import com.example.paging.MoviePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val appRepo: AppRepository,
) : ViewModel() {
    private val _detailsFlow = MutableStateFlow<Resource<MoviesResponse>>(Resource.Loading)
    val detailsFlow = _detailsFlow.asStateFlow()

    init { getMovies() }

    private fun getMovies() {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchMovieList("batman")
                _detailsFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _detailsFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }

    //Pagination using Paging 3 library
    val moviesFlow = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(appRepo) }
    ).flow.cachedIn(viewModelScope)
}