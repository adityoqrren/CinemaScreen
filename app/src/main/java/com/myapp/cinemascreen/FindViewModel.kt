package com.myapp.cinemascreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.data.MovieTVListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel() {

    var searchValue by mutableStateOf("")
        private set
    private val _recentSearch = MutableStateFlow<List<MovieTVListItem>>(emptyList())
    val recentSearch : StateFlow<List<MovieTVListItem>> get() = _recentSearch

    init {
        _recentSearch.value = cinemaScreenRepository.getPopularMovieTheaters()
    }

    private val genresData = listOf<String>(
        "Action and adventure",
        "Comedy",
        "Documentary",
        "Drama",
        "Fantasy",
        "Horror",
        "Kids",
        "Mistery and Thrillers",
        "Romance",
        "Science Fiction"
    )

    var genres = mutableStateListOf("")
        private set


    init {
        genres.clear()
        genres.addAll(genresData.subList(0,6))
    }

    fun toSetSearchValue(newSearchValue: String) {
        searchValue = newSearchValue
    }

    fun expandingGenres() {
        genres.addAll(genresData.subList(6,genresData.size))
    }

    fun clearSearchValue(){
        searchValue = ""
    }

}

