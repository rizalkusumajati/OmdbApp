package com.example.omdbapp.data.api.models

import com.example.omdbapp.domain.MovieHeader
import com.example.omdbapp.domain.MovieSearch
import com.squareup.moshi.Json


data class MovieResponse(

	@field:Json(name="Response")
	val response: String? = null,

	@field:Json(name="totalResults")
	val totalResults: String? = null,

	@field:Json(name="Search")
	val search: List<SearchItem> = emptyList()
)

fun MovieResponse.toDomain() =
	MovieSearch(
		response = response ?: "",
		totalResults = totalResults ?: "",
		listHeader = search.map { it.toDomain() }
	)

data class SearchItem(

	@field:Json(name="Type")
	val type: String? = null,

	@field:Json(name="Year")
	val year: String? = null,

	@field:Json(name="imdbID")
	val imdbID: String? = null,

	@field:Json(name="Poster")
	val poster: String? = null,

	@field:Json(name="Title")
	val title: String? = null
)

fun SearchItem.toDomain() =
	MovieHeader(
		type = type ?: "",
		year = year ?: "",
		imdbID = imdbID ?: "",
		poster = poster ?: "",
		title = title ?: ""
	)
