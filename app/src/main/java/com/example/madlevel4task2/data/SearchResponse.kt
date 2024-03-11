package com.example.madlevel4task2.data

import com.example.madlevel4task2.data.model.Movie

data class SearchResponse(
    val results: List<Movie>,
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
)

