package com.example.madlevel4task2.data.api

import com.example.madlevel4task2.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<ApiResponse<List<Movie>>>
}