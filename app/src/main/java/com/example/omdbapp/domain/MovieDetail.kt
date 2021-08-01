package com.example.omdbapp.domain


data class MovieDetail(
    val metascore: String,

    val boxOffice: String,

    val website: String,

    val imdbRating: String,

    val imdbVotes: String,

    val ratings: List<Rating>,

    val runtime: String,

    val language: String,

    val rated: String,

    val production: String,

    val released: String,

    val imdbID: String,

    val plot: String,

    val director: String,

    val title: String,

    val actors: String,

    val response: String,

    val type: String,

    val awards: String,

    val dVD: String,

    val year: String,

    val poster: String,

    val country: String,

    val genre: String,

    val writer: String
)

data class Rating(
    val value: String,

    val source: String
)
