package com.example.madlevel4task2.data.model

data class Movie(
    val backdrop_path: String?,
    val poster_path: String?,
    val title: String,
    val release_date: String,
    val popularity: Double,
    val overview: String
)