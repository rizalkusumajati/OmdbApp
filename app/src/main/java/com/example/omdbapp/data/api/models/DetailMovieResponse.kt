package com.example.omdbapp.data.api.models

import com.example.omdbapp.domain.MovieDetail
import com.example.omdbapp.domain.Rating
import com.squareup.moshi.Json

data class DetailMovieResponse(

	@field:Json(name="Metascore")
	val metascore: String? = null,

	@field:Json(name="BoxOffice")
	val boxOffice: String? = null,

	@field:Json(name="Website")
	val website: String? = null,

	@field:Json(name="imdbRating")
	val imdbRating: String? = null,

	@field:Json(name="imdbVotes")
	val imdbVotes: String? = null,

	@field:Json(name="Ratings")
	val ratings: List<RatingsItem> = emptyList(),

	@field:Json(name="Runtime")
	val runtime: String? = null,

	@field:Json(name="Language")
	val language: String? = null,

	@field:Json(name="Rated")
	val rated: String? = null,

	@field:Json(name="Production")
	val production: String? = null,

	@field:Json(name="Released")
	val released: String? = null,

	@field:Json(name="imdbID")
	val imdbID: String? = null,

	@field:Json(name="Plot")
	val plot: String? = null,

	@field:Json(name="Director")
	val director: String? = null,

	@field:Json(name="Title")
	val title: String? = null,

	@field:Json(name="Actors")
	val actors: String? = null,

	@field:Json(name="Response")
	val response: String? = null,

	@field:Json(name="Type")
	val type: String? = null,

	@field:Json(name="Awards")
	val awards: String? = null,

	@field:Json(name="DVD")
	val dVD: String? = null,

	@field:Json(name="Year")
	val year: String? = null,

	@field:Json(name="Poster")
	val poster: String? = null,

	@field:Json(name="Country")
	val country: String? = null,

	@field:Json(name="Genre")
	val genre: String? = null,

	@field:Json(name="Writer")
	val writer: String? = null
)

fun DetailMovieResponse.toDomain() =
	MovieDetail(
		metascore = metascore ?: "",
		boxOffice = boxOffice ?: "",
		website = website ?: "",
		imdbRating = imdbRating ?: "",
		imdbVotes = imdbVotes ?: "",
		ratings = ratings.map { it.toDomain() },
		runtime = runtime ?: "",
		language = language ?: "",
		rated = rated ?: "",
		production = production ?: "",
		released = released ?: "",
		imdbID = imdbID ?: "",
		plot = plot ?: "",
		director = director ?: "",
		title = title ?: "",
		actors = actors ?: "",
		response = response ?: "",
		type = type ?: "",
		awards = awards ?: "",
		dVD = dVD ?: "",
		year = year ?: "",
		poster = poster ?: "",
		country = country ?: "",
		genre = genre ?: "",
		writer = writer ?: ""
	)

data class RatingsItem(

	@field:Json(name="Value")
	val value: String? = null,

	@field:Json(name="Source")
	val source: String? = null
)

fun RatingsItem.toDomain() =
	Rating(
		value = value ?: "",
		source = source ?: ""
	)

