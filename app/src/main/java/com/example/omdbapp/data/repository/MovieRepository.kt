package com.example.omdbapp.data.repository

import com.example.omdbapp.TestIdlingResource
import com.example.omdbapp.data.api.models.ApiResult
import com.example.omdbapp.data.api.models.toDomain
import com.example.omdbapp.data.api.services.ApiService
import com.example.omdbapp.domain.*
import com.example.omdbapp.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class MovieRepository @Inject constructor(
    val apiService: ApiService
) {

    suspend fun getMovieList(searchKey: String, page: Int): Flow<UIState<MovieSearch>> = flow {
        try {
            emit(UIState.Loading)
            val apiResult = safeApiCall { apiService.getMovieList(
                searchKey = searchKey,
                page = page
            ) }

            when (apiResult){
                is ApiResult.OnSuccess -> {
                    TestIdlingResource.decrement("GLOBAL")
                    emit(UIState.SuccessFromRemote(
                        apiResult.response.toDomain()
                    ))
                }
                is ApiResult.NetworkError -> {
                    TestIdlingResource.decrement("GLOBAL")
                    emit(UIState.Error(apiResult.message))
                }
                is ApiResult.OnFailure -> {
                    TestIdlingResource.decrement("GLOBAL")
                    emit(UIState.Error(apiResult.errorMessage))
                }
            }
        }catch (e: Exception){
            TestIdlingResource.decrement("GLOBAL")
            emit(UIState.Error(e.message))
        }
    }

    suspend fun getMovieDetail(omdbKey: String): Flow<UIState<MovieDetail>> = flow {
        try {
            emit(UIState.Loading)
            val apiResult = safeApiCall { apiService.getMovieDetail(
               id = omdbKey
            ) }

            when (apiResult){
                is ApiResult.OnSuccess -> {
                    emit(UIState.SuccessFromRemote(
                        apiResult.response.toDomain()
                    ))
                }
                is ApiResult.NetworkError -> {
                    emit(UIState.Error(apiResult.message))
                }
                is ApiResult.OnFailure -> { emit(UIState.Error(apiResult.errorMessage))}

            }
        }catch (e: Exception){
            emit(UIState.Error(e.message))
        }
    }

}
