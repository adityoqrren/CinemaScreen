package com.myapp.cinemascreen.data.models

data class MovieData(
    val dates: Dates?,
    val page: Int,
    val results: List<MovieListItem>
)

