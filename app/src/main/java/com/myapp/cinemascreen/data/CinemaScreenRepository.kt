package com.myapp.cinemascreen.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CinemaScreenRepository @Inject constructor(
    private val cinemaScreenDataSource: CinemaScreenDataSource
) {
    init {
        Log.d("check DI", "masuk CinemaScreenRepository")
    }

    fun getTrendingNow(): List<MovieListItem> {
        Log.d("check DI", "masuk getTrendingNow() di repository")
        return cinemaScreenDataSource.generateTrendingNow
    }

    fun getNowInCinemas(): List<MovieListItem> {
        return cinemaScreenDataSource.generateNowInCinemas
    }

    fun getAiringNow(): List<TVListItem> {
        return cinemaScreenDataSource.generateAiringNow
    }

    suspend fun getPopularStreaming(): List<MovieTVListItem> {
        val list = withContext(Dispatchers.IO) {
            //async is run based on context of the parent coroutine (in this case Dispatchers.IO)
            val getPopularMovieStreaming = async{getPopularMovieStreaming()}

            val getPopularTVStreaming = async{getPopularTVStreaming()}

            getPopularMovieStreaming.await() + getPopularTVStreaming.await()
        }
        return list
    }

    suspend fun getPopularRent(): List<MovieTVListItem> {
        val list = withContext(Dispatchers.IO) {
            //async is run based on context of the parent coroutine (in this case Dispatchers.IO)
            val getPopularMovieRent = async{getPopularMovieRent()}

            val getPopularTVRent = async{getPopularTVRent()}

            getPopularMovieRent.await() + getPopularTVRent.await()
        }
        return list
    }

    fun getPopularMovieTheaters(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularMovieTheaters.map { movieItem ->
            MovieTVListItem(
                id = movieItem.id,
                title = movieItem.title,
                backdrop_path = movieItem.backdrop_path,
                poster_path = movieItem.poster_path,
                media_type = "movie"
            )
        }
    }

    fun getPopularTVonTV(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularTVonTV.map { tvItem ->
            MovieTVListItem(
                id = tvItem.id,
                title = tvItem.name,
                backdrop_path = tvItem.backdrop_path,
                poster_path = tvItem.poster_path,
                media_type = "tv"
            )
        }
    }

    fun getPopularMovieStreaming(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularMovieStream.map { movieItem ->
            MovieTVListItem(
                id = movieItem.id,
                title = movieItem.title,
                backdrop_path = movieItem.backdrop_path,
                poster_path = movieItem.poster_path,
                media_type = "movie"
            )
        }
    }

    fun getPopularTVStreaming(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularTVStream.map { tvItem ->
            MovieTVListItem(
                id = tvItem.id,
                title = tvItem.name,
                backdrop_path = tvItem.backdrop_path,
                poster_path = tvItem.poster_path,
                media_type = "tv"
            )
        }
    }

    fun getPopularMovieRent(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularMovieRent.map { movieItem ->
            MovieTVListItem(
                id = movieItem.id,
                title = movieItem.title,
                backdrop_path = movieItem.backdrop_path,
                poster_path = movieItem.poster_path,
                media_type = "movie"
            )
        }
    }

    fun getPopularTVRent(): List<MovieTVListItem> {
        return cinemaScreenDataSource.generatePopularTVRent.map { tvItem ->
            MovieTVListItem(
                id = tvItem.id,
                title = tvItem.name,
                backdrop_path = tvItem.backdrop_path,
                poster_path = tvItem.poster_path,
                media_type = "tv"
            )
        }
    }

}