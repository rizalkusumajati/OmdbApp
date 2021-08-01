package com.example.omdbapp.data.api.services

import com.example.omdbapp.data.api.models.DetailMovieResponse
import com.example.omdbapp.data.api.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object{
        const val API_KEY = "b9bd48a6"
        const val TYPE = "Movie"
    }
    @GET("/")
    suspend fun getMovieList(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("s") searchKey: String,
        @Query("type") type: String = TYPE,
        @Query("page") page: Int
    ): MovieResponse

    @GET("/")
    suspend fun getMovieDetail(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("i") id: String
    ): DetailMovieResponse
}
