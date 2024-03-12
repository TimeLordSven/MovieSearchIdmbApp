package com.example.madlevel4task2.repository
//Author: Sven Molenaar
import com.example.madlevel4task2.data.api.Api
import com.example.madlevel4task2.data.api.ApiService
import com.example.madlevel4task2.data.api.util.Resource
import com.example.madlevel4task2.data.model.Movie
import kotlinx.coroutines.withTimeout

class MovieRepository {
    private val _moviesApiService: ApiService = Api.tmdbClient

    suspend fun searchMovies(query: String): Resource<List<Movie>> {
        return try {
            withTimeout(5_000) {
                val response = _moviesApiService.searchMovies(query, Api.getApiKey())
                Resource.Success(response.body()!!.results)

            }
        } catch (e: Exception) {
            Resource.Error("An unknown error occurred while fetching data from the MovieAPI.")
        }
    }
}