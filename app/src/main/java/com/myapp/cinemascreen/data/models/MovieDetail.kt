package com.myapp.cinemascreen.data.models

data class MovieDetail(
    val adult: Boolean,
    val backdrop_path: String?,
    val budget: Int, //special in movie detail
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String?, //special in movie detail
    val original_language: String,
    val original_title: String, //special in movie detail
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String, //special in movie detail
    val runtime: Int, //special in movie detail
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val title: String,
    val video: Boolean, //special in movie detail
    val videos: Videos,
    val vote_average: Double,
    val vote_count: Int,
    val cast: List<Cast>?,
    val importantCrews: ImportantCrews?
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)

data class Videos(
    val results: List<VideoResult>
)

data class VideoResult(
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val site: String,
    val size: Int,
    val type: String
)