package com.myapp.cinemascreen.data.source.remote

import com.myapp.cinemascreen.data.models.TVDetail
import android.content.Context
import android.util.Log
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.source.remote.response.MediaListItem
import com.myapp.cinemascreen.data.source.remote.response.MovieCreditResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService
) {
    init {
        Log.d("check DI", "masuk CinemaScreenDataSource")
    }

    fun getTrendingMovies(): Flow<ApiResponse<List<MovieListItem>>> {
        return flow {
            try {
                val response = apiService.getTrendingMovies().results.filter {
                    it.media_type != "person"
                }
                if (response.isNotEmpty()) {
                    //kotlinx.coroutines.delay(2000)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getTrendingMovies()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailMovie(movieId: Int): Flow<ApiResponse<MovieDetail>> {
        return flow {
            try {
                val response = apiService.getDetailMovie(movieId = movieId)
                if (response.title.isNotEmpty()) {
                    //kotlinx.coroutines.delay(2000)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getTrendingMovies()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailTV(tvId: Int): Flow<ApiResponse<TVDetail>> {
        return flow {
            try {
                val response = apiService.getDetailTV(tvId = tvId)
                if (response.name.isNotEmpty()) {
                    //kotlinx.coroutines.delay(2000)
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getTrendingMovies()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getCredit(mediaType: String, movieId: Int): Flow<ApiResponse<MovieCreditResponse>> {
        Log.d("check mediaType in getCredit()", "mediaType: $mediaType")
        return flow {
            try {
                val response = apiService.getCredit(media_type = mediaType, movie_id = movieId)
                //kotlinx.coroutines.delay(2000)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getCredit()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchMovieTV(
        mediaType: String,
        query: String,
        page: Int
    ): Flow<ApiResponse<List<MediaListItem>>> {
        Log.d(
            "check params searchMovieTV() in RemoteDataSource ",
            "mediaType: $mediaType, | query $query | page $page"
        )
        return flow {
            try {
                val response =
                    apiService.searchMovieTV(query = query, media_type = mediaType, page = page)
//                if(response.results.isNotEmpty()){
                //Log.d("check result RemoteDataSource","${response.results}")
                emit(ApiResponse.Success(response.results))
//                }else{
//                    emit(ApiResponse.Empty)
//                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getTrendingMovies()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getPopularMovieTV(
        mediaType: String,
        watchRegion: String,
        watchMonetizationTypes: String = "",
        releaseType: String = ""
    ): Flow<ApiResponse<List<MediaListItem>>> {
        return flow {
            try {
                val response = apiService.getPopularMovieTV(
                    mediaType = mediaType,
                    watchRegion = watchRegion,
                    watchMonetizationTypes = watchMonetizationTypes,
                    releaseType = releaseType
                ).results
//                if(response.isNotEmpty()){
//                    //kotlinx.coroutines.delay(2000)
                emit(ApiResponse.Success(response))
//                }else{
//                    emit(ApiResponse.Empty)
//                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getPopularMovieTV()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getPlayingNowMovies(): Flow<ApiResponse<List<MediaListItem>>> {
        return flow {
            try {
                val response = apiService.getNowPlayingMovies().results
//                if(response.isNotEmpty()){
//                    //kotlinx.coroutines.delay(2000)
                emit(ApiResponse.Success(response))
//                }else{
//                    emit(ApiResponse.Empty)
//                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getPlayingNowMovies()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAiringTodayTV(): Flow<ApiResponse<List<MediaListItem>>> {
        return flow {
            try {
                val response = apiService.getAiringTodayTV().results
//                if(response.isNotEmpty()){
//                    //kotlinx.coroutines.delay(2000)
                emit(ApiResponse.Success(response))
//                }else{
//                    emit(ApiResponse.Empty)
//                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource getAiringNowTV()", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}