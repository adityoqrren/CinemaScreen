package com.myapp.cinemascreen.data.models

data class TVDetail(
    val adult: Boolean,
    val backdrop_path: String?,
    val created_by: List<Creator>, //special in tv detail
    val episode_run_time: List<Int>, //special in tv detail
    val first_air_date: String?, //special in tv detail
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val in_production: Boolean, //special in tv detail
    val languages: List<String>,
    val last_air_date: String?, //special in tv detail
    val last_episode_to_air: Episode?, //special in tv detail
    val name: String, //special in tv detail
    val next_episode_to_air: Episode?, //special in tv detail
    val networks: List<Network>, //special in tv detail
    val number_of_episodes: Int, //special in tv detail
    val number_of_seasons: Int, //special in tv detail
    val origin_country: List<String>, //special in tv detail
    val original_language: String,
    val original_name: String, //special in tv detail
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>, //special in tv detail
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val type: String, //special in tv detail
    val vote_average: Double,
    val vote_count: Int,
    val videos: Videos?,
    val cast: List<Cast>?,
    val executiveProducers: List<Crew>?
)

data class Creator(
    val id: Int,
    val credit_id: String,
    val name: String,
    val original_name: String,
    val gender: Int,
    val profile_path: String?
)


data class Episode(
    val id: Int,
    val name: String,
    val overview: String?,
    val vote_average: Double,
    val vote_count: Int,
    val air_date: String?,
    val episode_number: Int,
    val episode_type: String?,
    val production_code: String?,
    val runtime: Int?,
    val season_number: Int,
    val show_id: Int,
    val still_path: String?
)

data class Network(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

data class Season(
    val air_date: String?,
    val episode_count: Int,
    val id: Int,
    val name: String,
    val overview: String?,
    val poster_path: String?,
    val season_number: Int,
    val vote_average: Double
)


