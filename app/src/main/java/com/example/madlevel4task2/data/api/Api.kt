package com.example.madlevel4task2.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "1964712006647770f1ae4a8c49219b45"
//ToDo make LocalVariable

        val tmdbClient by lazy { createApi(TMDB_BASE_URL, API_KEY) }

        private fun createApi(baseUrl: String, apiKey: String): ApiService {
            val client = OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                readTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
            }.build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        fun getApiKey(): String {
            return API_KEY
        }
    }
}