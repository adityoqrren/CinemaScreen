package com.myapp.cinemascreen.data.util

import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.MovieTVListItem
import com.myapp.cinemascreen.utils.Converters.movieListItemForMappingEntity
import com.myapp.cinemascreen.utils.Converters.movieTVListItemForMappingEntity
import com.myapp.cinemascreen.data.source.local.entity.MovieListItemEntity
import com.myapp.cinemascreen.data.source.remote.response.MediaListItem

object DataMapper {
    //mapping MovieListItemEntity to MovieListItem
    fun mapMovieItemEntityToItem(input: MovieListItemEntity) : MovieListItem =
        movieListItemForMappingEntity.copy(
            id = input.id,
            poster_path = input.poster_path,
            media_type = input.media_type,
            time_added = input.time_added
        )

    //mapping MovieListItemEntity to MovieTVListItem
    fun mapMovieItemEntityToMovieTVListItem(input: MovieListItemEntity) : MovieTVListItem =
        movieTVListItemForMappingEntity.copy(
            id = input.id,
            media_type = input.media_type,
            poster_path = input.poster_path
        )

    //mapping MediaListItem to MovieTVListItem (movie)
    fun mapMediaListItemtoMovieTVListItem(item: MediaListItem, media_type : String = "") = MovieTVListItem(
        item.id,
        item.name ?: item.title ?: "",
        item.poster_path ?: "",
        item.media_type ?: media_type,
        item.first_air_date ?: item.release_date ?: "",
        item.popularity,
        item.vote_average
    )


    //mapping MovieListItem to MovieListItemEntity
//    fun mapMovieItemToEntity(input: MovieListItem) : MovieListItemEntity = MovieListItemEntity(
//        id = input.id,
////        adult = input.adult,
////        backdrop_path = input.backdrop_path,
////        genre_ids = intListToString(input.genre_ids),
////        original_language \= input.original_language,
////        original_title = input.original_title,
////        overview = input.overview,
////        popularity = input.popularity,
//        poster_path = input.poster_path,
////        release_date = input.release_date,
////        title = input.title,
//        media_type = input.media_type,
////        video = input.video,
////        vote_average = input.vote_average,
////        vote_count = input.vote_count
//    )
}