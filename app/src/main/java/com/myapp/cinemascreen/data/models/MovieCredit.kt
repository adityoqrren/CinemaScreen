package com.myapp.cinemascreen.data.models

data class MovieCredit(
    val id: Int,
    val cast: List<Cast>,
    val importantCrews: ImportantCrews
)
