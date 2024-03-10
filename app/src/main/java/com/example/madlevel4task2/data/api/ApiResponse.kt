package com.example.madlevel4task2.data.api

data class ApiResponse<T>(
    val results: T?,
    val error: String?
)