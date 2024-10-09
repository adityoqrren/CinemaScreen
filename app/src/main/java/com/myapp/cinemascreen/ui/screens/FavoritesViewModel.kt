package com.myapp.cinemascreen.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.myapp.cinemascreen.utils.MediaType
import com.myapp.cinemascreen.data.CinemaScreenRepository
import com.myapp.cinemascreen.data.models.MovieTVFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {
//    private val _favoritesMovieTV = MutableStateFlow<List<MovieTVListItem>>(emptyList())
//    val favoritesMovieTV : StateFlow<List<MovieTVListItem>> get() = _favoritesMovieTV

    private lateinit var listenerRegistration: ListenerRegistration

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _idSelected = MutableStateFlow<Int>(1)
    val idSelected: StateFlow<Int> get() = _idSelected

    private var listFavoriteAll : MutableStateFlow<List<MovieTVFavorite>> = MutableStateFlow(emptyList())
    private var listFavoriteMovies : MutableStateFlow<List<MovieTVFavorite>> = MutableStateFlow(
        emptyList()
    )
    private var listFavoriteTV : MutableStateFlow<List<MovieTVFavorite>> = MutableStateFlow(emptyList())


    //buat UI state

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoritesMovieTV: StateFlow<List<MovieTVFavorite>> = idSelected.flatMapLatest { id ->
        when(id){
            1 -> listFavoriteAll
            2 -> listFavoriteMovies
            else -> listFavoriteTV
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
    //because mapLatest mapping Flow to Flow, we must use stateIn to transform Flow to StateFlow

    init {
        //getFavorites()
        Log.d("FavoritesViewModel","FavoritesViewModel created")
        getFavorites()
    }

    private fun getFavorites(){
        viewModelScope.launch {
            auth.uid?.let {
                uid ->
            listenerRegistration =  db.collection("users")
                    .document(uid)
                    .collection("favorites")
                    .addSnapshotListener{
                        value, e ->
                        if(e!=null){
                            Log.w("getFavorites in FavoritesViewModel","Listen failed", e)
                            return@addSnapshotListener
                        }
                        val favorites = mutableListOf<MovieTVFavorite>()
                        for (doc in value!!){
                            val id = doc.getLong("id")!!.toInt()
                            val title = doc.getString("title").toString()
                            val posterPath = doc.getString("poster_path").toString()
                            val mediaType = doc.getString("media_type").toString()

                            favorites.add(
                                MovieTVFavorite(id, title, posterPath, mediaType)
                            )
                        }
                        listFavoriteAll.value = favorites
                        listFavoriteMovies.value = favorites.filter {
                            it.media_type == MediaType.Movie
                        }
                        listFavoriteTV.value = favorites.filter {
                            it.media_type == MediaType.TV
                        }
                    }
            }
        }
    }

    fun detachListener(){
        if(::listenerRegistration.isInitialized){
            listenerRegistration.remove()
        }
    }

//    val favoritesMovieTV = _idSelected.mapLatest {
//        when(it){
//            1 -> listOf(cinemaScreenRepository.getPopularTVonTV(),cinemaScreenRepository.getPopularMovieTheaters()).flatten()
//            2 -> cinemaScreenRepository.getPopularMovieTheaters()
//            else -> cinemaScreenRepository.getPopularTVonTV()
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000L),
//        initialValue = emptyList()
//    )

    fun setIdSelected(id: Int){
        _idSelected.value = id
    }

}