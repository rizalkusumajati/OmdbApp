package com.example.omdbapp.domain


data class MovieSearch(
    val response: String,
    val totalResults: String,
    val listHeader: MovieHeaders
)


data class MovieHeader(
    val type: String,
    val year : String,
    val imdbID: String,
    val poster: String,
    val title: String
)

typealias MovieHeaders = List<MovieHeader>
