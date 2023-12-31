package com.myapp.cinemascreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.data.MovieListItem
import com.myapp.cinemascreen.data.TVListItem
import com.myapp.cinemascreen.data.MovieTVListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel(){
    private val _trendingNow = MutableStateFlow<List<MovieListItem>>(emptyList())
    val trendingNow : StateFlow<List<MovieListItem>> get() = _trendingNow
    private val _nowInCinemas = MutableStateFlow<List<MovieListItem>>(emptyList())
    val nowInCinemas : StateFlow<List<MovieListItem>> get() = _nowInCinemas
    private val _airingNow = MutableStateFlow<List<TVListItem>>(emptyList())
    val airingNow : StateFlow<List<TVListItem>> get() = _airingNow

    private val _popularMovieTV = MutableStateFlow<List<MovieTVListItem>>(emptyList())
    val popularMovieTV : StateFlow<List<MovieTVListItem>> get() = _popularMovieTV

    init {
        Log.d("check DI","masuk HomeScreenViewModel")
        _trendingNow.value = cinemaScreenRepository.getTrendingNow()
        _nowInCinemas.value = cinemaScreenRepository.getNowInCinemas()
        _airingNow.value = cinemaScreenRepository.getAiringNow()
    }

    fun getPopular(popularParams: Int){
        viewModelScope.launch {
            when(popularParams){
                1 -> _popularMovieTV.value = cinemaScreenRepository.getPopularStreaming()
                2 -> _popularMovieTV.value = cinemaScreenRepository.getPopularTVonTV()
                3 -> _popularMovieTV.value = cinemaScreenRepository.getPopularRent()
                4 -> _popularMovieTV.value = cinemaScreenRepository.getPopularMovieTheaters()
            }
//            if(popularParams==1){
//                _popularMovieTV.value = cinemaScreenRepository.getPopularStreaming()
//            }
        }
    }
}