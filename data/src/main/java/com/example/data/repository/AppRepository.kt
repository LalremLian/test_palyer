package com.example.data.repository

import com.example.network.api.ApiInterface
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val api: ApiInterface,
) {
    suspend fun fetchDetails(movieTitle: String) = api.getDetails(title = movieTitle)
    suspend fun fetchMovieList(
        movieTitle: String,
        page: Int? = 1
    ) = api.getListOfBatman(
        title = movieTitle,
        page = page!!
    )
}