package com.myapp.cinemascreen.data.source.remote.response

import com.myapp.cinemascreen.data.models.MovieListItem

data class MovieListResponse (
    val page:Int,
    val results:List<MovieListItem>
)