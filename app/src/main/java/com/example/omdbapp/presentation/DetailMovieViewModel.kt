package com.example.omdbapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbapp.data.repository.MovieRepository
import com.example.omdbapp.domain.MovieDetail
import com.example.omdbapp.domain.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    val repository: MovieRepository
): ViewModel() {

    private val _detailMovie: MutableLiveData<UIState<MovieDetail>> = MutableLiveData()
    val detailMovie: LiveData<UIState<MovieDetail>> = _detailMovie

    fun getDetailMovie(omdbKey : String){
        viewModelScope.launch {
            repository.getMovieDetail(omdbKey)
                .collect {
                    _detailMovie.value = it
                }
        }
    }

}
