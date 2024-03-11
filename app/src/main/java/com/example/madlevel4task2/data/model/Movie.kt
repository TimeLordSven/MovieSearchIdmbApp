package com.example.madlevel4task2.data.model

data class Movie(
    val backdropPath: String?,
    val posterPath: String?,
    val title: String,
    val releaseDate: String,
    val rating: Double,
    val overview: String
)