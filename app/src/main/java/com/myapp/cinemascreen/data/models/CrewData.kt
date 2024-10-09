package com.myapp.cinemascreen.data.models

data class CastData(
    val cast: List<Cast>,
    val id: Int
)

data class CrewData(
    val crew: List<Crew>,
    val id: Int
)

data class Cast(
    val adult: Boolean,
    val cast_id: Int, //no in crew
    val character: String, //no in crew
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int, //no in crew
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)

data class Crew(
    val adult: Boolean,
    val credit_id: String,
    val department: String, //no in cast
    val gender: Int,
    val id: Int,
    val job: String, //no in cast
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)

data class ImportantCrews(
    val directors : List<String>,
    val writers : List<String>?,
    val screenplay: List<String>?,
)
