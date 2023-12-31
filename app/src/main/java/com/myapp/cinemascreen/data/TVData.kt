package com.myapp.cinemascreen.data

data class TVData(
    val page: Int,
    val results: List<TVListItem>,
    val total_pages: Int,
    val total_results: Int
)