package com.myapp.cinemascreen.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.MovieTVListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindViewModel @Inject constructor(
    private val cinemaScreenRepository: CinemaScreenRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> get() = _searchQuery

    private val _recentSearch = MutableStateFlow<List<MovieListItem>>(emptyList())
    val recentSearch : StateFlow<List<MovieListItem>> get() = _recentSearch

    val isInSearch = mutableStateOf(false)

    private val _idCatSelected = MutableStateFlow<Int>(1)
    val idCatSelected: StateFlow<Int> get() = _idCatSelected

//    private var cachedSearchResults : List<MovieTVListItem> = emptyList()
//    private var latestQuery: String = ""

    val searchResult = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if(query.isEmpty()){
                flowOf(PagingData.empty())
            }else{
                cinemaScreenRepository.searchMovieTV(query)
            }
        }.cachedIn(viewModelScope)

    val filteredSearchResults = searchResult.combine(_idCatSelected){ pagingData, id ->
        pagingData.filter { item ->
            when(id){
                2 -> item.media_type == "movie"
                3 -> item.media_type == "tv"
                else -> true
            }
        }
    }

    init {
        //_recentSearch.value = cinemaScreenRepository.getPopularMovieTheaters()
        //Log.d("check isInSearch","value isInSearch init ${isInSearch.value}")
        getRecentSearch()
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

    fun setIsInSearch(boolean: Boolean){
        isInSearch.value = boolean
    }

    fun toSetSearchValue(newSearchValue: String) {
        _searchQuery.value = newSearchValue
    }

    fun toSetCatSelected(id: Int){
        _idCatSelected.value = id
    }

    fun getRecentSearch(){
        viewModelScope.launch{
            cinemaScreenRepository.getRecentSearch().collect {
                Log.d("check recent search saved","$it")
                _recentSearch.value = it
            }
        }
    }

    fun saveToRecentSearch(item: MovieTVListItem){
        viewModelScope.launch {
            cinemaScreenRepository.insertRecentSearch(item)
        }
    }

    fun expandingGenres() {
        genres.addAll(genresData.subList(6,genresData.size))
    }

    fun clearSearchValue(){
        _searchQuery.value = ""
    }

}

