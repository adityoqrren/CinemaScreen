package com.myapp.cinemascreen.utils

import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.MovieTVListItem

object Converters {

    //integer list to String
    fun intListToString(intList:List<Int>) : String{
        return intList.joinToString(separator = ",")
    }

    //String to Int List
    fun stringToIntList(intString: String) : List<Int>{
        return intString.split(",").map { it.toInt() }
    }

    val movieListItemForMappingEntity = MovieListItem(
        id = 0,
        adult = false,
        backdrop_path = "",
        genre_ids = listOf(),
        original_language = "",
        original_title = "",
        overview = "",
        popularity = 0.0,
        poster_path = "",
        release_date = "",
        title = "",
        media_type = "",
        video = false,
        vote_average = 0.0,
        vote_count = 0
    )

    val movieTVListItemForMappingEntity = MovieTVListItem(
        id = 0,
        media_type = "",
        poster_path = "",
        title = "",
        release_date = "",
        popularity = 0.0,
        vote_average = 0.0
    )
}