package com.example.madlevel4task2.data
//Author: Sven Molenaar
import com.example.madlevel4task2.data.model.Movie

data class SearchResponse(
    val results: List<Movie>,
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
)

