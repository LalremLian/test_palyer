package com.example.testapp.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Resource
import com.example.data.repository.AppRepository
import com.example.network.model.MovieDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsScreenViewModel @Inject constructor(
    private val appRepo: AppRepository
): ViewModel(){

    private val _detailsFlow = MutableStateFlow<Resource<MovieDetailsResponse>>(Resource.Loading)
    val detailsFlow = _detailsFlow.asStateFlow()

    fun getMoviesDetails(movieId: String) {
        viewModelScope.launch {
            try {
                val response = appRepo.fetchDetails(movieId)
                _detailsFlow.value = Resource.Success(response)
            } catch (e: Exception) {
                _detailsFlow.value = Resource.Error(e.message ?: "Failed to fetch data")
            }
        }
    }
}