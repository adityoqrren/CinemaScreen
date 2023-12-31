package com.myapp.cinemascreen.data

import android.content.Context
import android.util.Log
import com.myapp.cinemascreen.generateMovieList
import com.myapp.cinemascreen.generateMovieTVList
import com.myapp.cinemascreen.generateTVList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CinemaScreenDataSource @Inject constructor(
    @ApplicationContext private val context : Context
) {
    init {
        Log.d("check DI","masuk CinemaScreenDataSource")
    }
    val generateTrendingNow = generateMovieTVList("datajson/trending_today_list.json", context)
    val generateNowInCinemas = generateMovieList("datajson/movielist_nowplaying.json",context)
    val generateAiringNow = generateTVList("datajson/tvlist_airing_now.json", context)
    val generatePopularMovieStream = generateMovieList("datajson/movielist_popular_streaming.json", context)
    val generatePopularTVStream = generateTVList("datajson/tvlist_popular_streaming.json",context)
    val generatePopularMovieRent = generateMovieList("datajson/movielist_popular_rent.json", context)
    val generatePopularTVRent = generateTVList("datajson/tvlist_popular_rent.json",context)
    val generatePopularMovieTheaters = generateMovieList("datajson/movielist_popular_theaters.json", context)
    val generatePopularTVonTV = generateTVList("datajson/tvlist_popular_tv.json",context)
}