package com.myapp.cinemascreen.data.models

//this data class is unified form of Movie and TV.
//we make all fields the same, so there is no confusion
//just differentiate it by media_type
data class MovieTVListItem(
    val id: Int,
    val title:String,
    val poster_path: String,
    val media_type: String,
    val release_date: String,
    val popularity: Double,
    val vote_average: Double
)