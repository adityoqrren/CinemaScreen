package com.myapp.cinemascreen.data.source.remote.response

import com.myapp.cinemascreen.data.models.Cast
import com.myapp.cinemascreen.data.models.Crew

data class MovieCreditResponse(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
)
