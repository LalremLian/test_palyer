package com.example.network.api

import com.example.network.model.MovieDetailsResponse
import com.example.network.model.MoviesResponse
import com.example.util.API_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //----------------------------------Details
    @GET("/")
    suspend fun getDetails(
        @Query("t") title: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = API_TOKEN,
    ): MovieDetailsResponse

    //----------------------------------List of Batman
    @GET("/")
    suspend fun getListOfBatman(
        @Query("s") title: String,
        @Query("apikey") apiKey: String = API_TOKEN,
    ): MoviesResponse
}