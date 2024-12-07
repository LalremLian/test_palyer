package com.example.data.repository

import com.example.network.api.ApiInterface
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val api: ApiInterface,
) {
    // Fetch the details of a movie (API Repo)
    suspend fun fetchDetails(movieTitle: String) = api.getDetails(title = movieTitle)
    suspend fun fetchMovieList(
        movieTitle: String,
        year: String?,
        page: Int? = 1
    ) = api.getListOfBatman(
        title = movieTitle,
        year = year,
        page = page!!
    )
}