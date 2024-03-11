package com.example.madlevel4task2.repository

import android.util.Log
import com.example.madlevel4task2.data.api.Api
import com.example.madlevel4task2.data.api.ApiService
import com.example.madlevel4task2.data.api.util.Resource
import com.example.madlevel4task2.data.model.Movie
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import java.lang.Error

class MovieRepository {
    private val _moviesApiService: ApiService = Api.tmdbClient
    private val ApiKey = "1964712006647770f1ae4a8c49219b45"
    suspend fun searchMovies(query: String): Resource<Response<List<Movie>>> {
        return try {
            withTimeout(5_000) {
                val response = _moviesApiService.searchMovies(query, ApiKey)
                Resource.Success(response)
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", e.message ?: "No exception message available")
            Resource.Error("An unknown error occurred while fetching data from the MovieAPI.")
        }
    }
}