package com.myapp.cinemascreen.data.source.remote.response

data class MediaListResponse(
    val page:Int,
    val results:List<MediaListItem>
)

data class MediaListItem(
    val id: Int,
    val name: String?,
    val title: String?,
    val first_air_date: String?,
    val release_date: String?,
    val media_type: String?,
    val poster_path: String?,
    val popularity: Double,
    val vote_average: Double
)