package com.myapp.cinemascreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cinemascreen.data.CinemaScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel() {
//    private val _favoritesMovieTV = MutableStateFlow<List<MovieTVListItem>>(emptyList())
//    val favoritesMovieTV : StateFlow<List<MovieTVListItem>> get() = _favoritesMovieTV

    private val _idSelected = MutableStateFlow<Int>(1)
    val idSelected: StateFlow<Int> get() = _idSelected

    val favoritesMovieTV = _idSelected.mapLatest {
        when(it){
            1 -> listOf(cinemaScreenRepository.getPopularTVonTV(),cinemaScreenRepository.getPopularMovieTheaters()).flatten()
            2 -> cinemaScreenRepository.getPopularMovieTheaters()
            else -> cinemaScreenRepository.getPopularTVonTV()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun setIdSelected(id: Int){
        _idSelected.value = id
    }

}