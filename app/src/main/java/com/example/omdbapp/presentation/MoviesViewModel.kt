package com.example.omdbapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbapp.TestIdlingResource
import com.example.omdbapp.data.repository.MovieRepository
import com.example.omdbapp.domain.MovieSearch
import com.example.omdbapp.domain.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    val repository: MovieRepository
) : ViewModel() {

    private val _listMovie: MutableLiveData<UIState<MovieSearch>> = MutableLiveData()
    val listMovie: LiveData<UIState<MovieSearch>> = _listMovie

    private var _pageCount: Int = 1
    val pageCount get() = _pageCount

    var searchKeyGlobal: String = ""

    fun resetPageCount(){
        _pageCount = 1
    }
    fun incrementPageCount(){
        _pageCount++
    }
    fun getDetailMovie(searchKey: String) {
        searchKeyGlobal = searchKey
        TestIdlingResource.increment("GLOBAL")
        viewModelScope.launch {
            repository.getMovieList(
                searchKey = searchKey,
                page = pageCount
            ).collect {
                _listMovie.value = it
            }
        }
    }
}
