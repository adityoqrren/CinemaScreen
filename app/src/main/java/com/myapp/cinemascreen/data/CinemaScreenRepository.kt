package com.myapp.cinemascreen.data

import com.myapp.cinemascreen.data.models.TVDetail
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.myapp.cinemascreen.utils.MediaType
import com.myapp.cinemascreen.Resource
import com.myapp.cinemascreen.data.models.ImportantCrews
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.MovieTVListItem
import com.myapp.cinemascreen.data.source.local.LocalDataSource
import com.myapp.cinemascreen.data.source.paging.MovieTVPagingSource
import com.myapp.cinemascreen.data.source.remote.ApiResponse
import com.myapp.cinemascreen.data.source.remote.RemoteDataSource
import com.myapp.cinemascreen.data.util.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CinemaScreenRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    init {
        Log.d("check DI", "masuk CinemaScreenRepository")
    }

    //In memory caches
    private val trendingNowMutex = Mutex()
    private var trendingNow: List<MovieListItem> = emptyList()

    suspend fun getTrendingNow(): Flow<Resource<List<MovieListItem>>> {
        Log.d("check DI", "masuk getTrendingNow() di repository")
        when(val response = remoteDataSource.getTrendingMovies().first()){
            is ApiResponse.Success -> {
                Log.d("check getTrendingNow() rp","success")
                trendingNowMutex.withLock {
                    trendingNow = response.data
                }
                //delete all data first in localdb by category = 1 (trending)
                localDataSource.deleteMovieByCategory(categoryId = 1)
                //insert to localdb
                localDataSource.insertTrendingMovies(trendingNow)
                Log.d("see trending movies","$trendingNow")
                return trendingNowMutex.withLock { flowOf(Resource.Success(trendingNow))}
            }
            is ApiResponse.Empty -> {
                Log.d("check getTrendingNow() rp","empty")
                return flow {
                    emit(Resource.Success(trendingNow))
                }
            }
            is ApiResponse.Error -> {
                Log.d("check getTrendingNow() rp","error")
                val cachedData = localDataSource.getMoviesByCategoryId(1)
                //if error, return error message and cached data from localdb
                return flow {
                    emit(Resource.Error(message = response.errorMessage, data = cachedData))
                }
            }
        }
    }

    fun searchMovieTV(query:String) : Flow<PagingData<MovieTVListItem>>{
        return Pager(
            config = PagingConfig(pageSize = 12, prefetchDistance = 2),
            pagingSourceFactory = {
                MovieTVPagingSource(remoteDataSource, query)
            }
        ).flow
    }

    //delete all movie tv saved to local db
    suspend fun deleteAllMovieTVSaved(){
        localDataSource.deleteMovies()
    }

    suspend fun insertRecentSearch(item: MovieTVListItem){
        localDataSource.insertRecentSearch(item,System.currentTimeMillis())
    }

    fun getRecentSearch() : Flow<List<MovieListItem>>{
        return localDataSource.getMoviesByCategoryIdDesc(5)
    }

    //get popular streaming
    fun getPopularStreaming(): Flow<Resource<List<MovieTVListItem>>> {
        Log.d("check DI", "masuk getPopularStreaming() di repository")
        val popularStreamingTV = remoteDataSource.getPopularMovieTV(
            mediaType = "tv",
            watchRegion = "US",
            watchMonetizationTypes = "flatrate"
        )

        val popularStreamingMovie = remoteDataSource.getPopularMovieTV(
            mediaType = "movie",
            watchRegion = "US",
            watchMonetizationTypes = "flatrate"
        )

        return combine(popularStreamingTV, popularStreamingMovie) {
            streamTV, streamMovie ->
            when{
                (streamTV is ApiResponse.Success && streamMovie is ApiResponse.Success) -> {
                    val tv = streamTV.data.map {
                        DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.TV)
                    }
                    val movie = streamMovie.data.map {
                        DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.Movie)
                    }

                    val combinedList = tv.let {
                        if(it.size>0) it.subList(0,6) else it
                    } + movie.let {
                        if(it.size>0) it.subList(0,6) else it
                    }.sortedByDescending { it.popularity }

                    Resource.Success(combinedList)
                }
                (streamTV is ApiResponse.Error) -> {
                    Resource.Error(message = streamTV.errorMessage)
                }
                (streamMovie is ApiResponse.Error) -> {
                    Resource.Error(message = streamMovie.errorMessage)
                }
                else -> {
                    Resource.Error(message = "just error")
                }
            }
        }
    }

    //get popular rent
    fun getPopularRent(): Flow<Resource<List<MovieTVListItem>>> {
        Log.d("check DI", "masuk getPopularRent() di repository")
        val popularRentTV = remoteDataSource.getPopularMovieTV(
            mediaType = "tv",
            watchRegion = "US",
            watchMonetizationTypes = "rent"
        )

        val popularRentMovie = remoteDataSource.getPopularMovieTV(
            mediaType = "movie",
            watchRegion = "US",
            watchMonetizationTypes = "rent"
        )

        return combine(popularRentTV, popularRentMovie) {
                rentTV, rentMovie ->
            when{
                (rentTV is ApiResponse.Success && rentMovie is ApiResponse.Success) -> {
                    val tv = rentTV.data.map {
                        DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.TV)
                    }
                    val movie = rentMovie.data.map {
                        DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.Movie)
                    }

                    val combinedList = tv.let {
                        if(it.size>0) it.subList(0,6) else it
                    } + movie.let {
                        if(it.size>0) it.subList(0,6) else it
                    }.sortedByDescending { it.popularity }

                    Resource.Success(combinedList)
                }
                (rentTV is ApiResponse.Error) -> {
                    Resource.Error(message = rentTV.errorMessage)
                }
                (rentMovie is ApiResponse.Error) -> {
                    Resource.Error(message = rentMovie.errorMessage)
                }
                else -> {
                    Resource.Error(message = "just error")
                }
            }
        }
    }

    //get popular on Tv
    suspend fun getPopularTVonTV(): Flow<Resource<List<MovieTVListItem>>> {
        val response = remoteDataSource.getPopularMovieTV(
            mediaType = "tv",
            watchRegion = "US",
        ).first()

        when(response){
            is ApiResponse.Error -> {
                return flowOf(Resource.Error(response.errorMessage))
            }
            is ApiResponse.Success -> {
                return flowOf(Resource.Success(response.data.map {
                    DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.TV)
                }))
            }
            ApiResponse.Empty -> {return flow {  }} //this condition will never happen
        }
    }

    //get popular on cinema
    suspend fun getPopularMovieOnCinema(): Flow<Resource<List<MovieTVListItem>>> {
        val response = remoteDataSource.getPopularMovieTV(
            mediaType = "movie",
            watchRegion = "US",
            releaseType = "3|2"
        ).first()

        when(response){
            is ApiResponse.Error -> {
                return flowOf(Resource.Error(response.errorMessage))
            }
            is ApiResponse.Success -> {
                return flowOf(Resource.Success(response.data.map {
                    DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.Movie)
                }))
            }
            ApiResponse.Empty -> {return flow {  }} //this condition will never happen
        }
    }

    suspend fun getPlayingNowMovies(): Flow<Resource<List<MovieTVListItem>>> {
        when(val response = remoteDataSource.getPlayingNowMovies().first()){
            is ApiResponse.Error -> {
                return flowOf(Resource.Error(response.errorMessage))
            }
            is ApiResponse.Success -> {
                return flowOf(Resource.Success(response.data.map {
                    DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.Movie)
                }))
            }
            ApiResponse.Empty -> {return flow {  }} //this condition will never happen
        }
    }

    suspend fun getAiringTodayTV(): Flow<Resource<List<MovieTVListItem>>> {
        when(val response = remoteDataSource.getAiringTodayTV().first()){
            is ApiResponse.Error -> {
                return flowOf(Resource.Error(response.errorMessage))
            }
            is ApiResponse.Success -> {
                return flowOf(Resource.Success(response.data.map {
                    DataMapper.mapMediaListItemtoMovieTVListItem(it, MediaType.TV)
                }))
            }
            ApiResponse.Empty -> {return flow {  }} //this condition will never happen
        }
    }

    suspend fun getDetailMovie(media_type:String, movieId: Int) : Flow<Resource<MovieDetail>>{
        Log.d("check DI", "masuk getTrendingNow() di repository")
        when(val response = remoteDataSource.getDetailMovie(movieId).first()){
            is ApiResponse.Success -> {
                //Log.d("check getTrendingNow() rp","success")
                //flowOf(Resource.Success(response.data))
                when(val responseCredit = remoteDataSource.getCredit(mediaType = media_type, movieId = movieId).first()){
                    is ApiResponse.Empty -> {
                        return flowOf(Resource.Error(message = "empty in response credit"))
                    }
                    is ApiResponse.Error -> {
                        return flowOf(Resource.Error(message = responseCredit.errorMessage))
                    }
                    is ApiResponse.Success -> {
                        return flow {
                            val cast = responseCredit.data.cast
                            val crew = responseCredit.data.crew
                            val filterOnlyDirectors = crew.filter { it.known_for_department == "Directing" && it.job == "Director"}.map { it.name }
                            val filterOnlyWriters = crew.filter { it.known_for_department == "Writing" && it.job == "Writer"}.map { it.name }
                            val filterOnlyScreenplay = crew.filter { it.known_for_department == "Writing" && it.job == "Screenplay"}.map { it.name }
                            val importantCrews = ImportantCrews(filterOnlyDirectors, filterOnlyWriters,filterOnlyScreenplay)
                            //Log.d("check getDetailMovie() inner flow","response data: ${response.data}")
                            val responseCopy = response.data.copy(
                                cast = cast,
                                importantCrews = importantCrews
                            )
                            //Log.d("check getDetailMovie() inner flow","response copied : $responseCopy")
                            emit(
                                Resource.Success(
                                    data = responseCopy
                                )
                            )
                        }.flowOn(Dispatchers.IO)
                    }
                }
            }

            is ApiResponse.Empty -> {
                //Log.d("check getTrendingNow() rp","empty")
                return flowOf(Resource.Error(message = "empty"))
            }

            is ApiResponse.Error -> {
                return flow {
                    emit(Resource.Error(message = response.errorMessage))
                }
            }
        }
    }

    suspend fun getDetailTV(tvId: Int) : Flow<Resource<TVDetail>>{
        Log.d("check DI", "masuk getTrendingNow() di repository")
        when(val response = remoteDataSource.getDetailTV(tvId).first()){
            is ApiResponse.Success -> {
                //Log.d("check getTrendingNow() rp","success")
                //flowOf(Resource.Success(response.data))
                when(val responseCredit = remoteDataSource.getCredit(mediaType = MediaType.TV, movieId = tvId).first()){
                    is ApiResponse.Empty -> {
                        return flowOf(Resource.Error(message = "empty in response credit"))
                    }
                    is ApiResponse.Error -> {
                        return flowOf(Resource.Error(message = responseCredit.errorMessage))
                    }
                    is ApiResponse.Success -> {
                        return flow {
                            val cast = responseCredit.data.cast
                            val crew = responseCredit.data.crew
                            val filterOnlyExecutiveProducers = crew.filter { it.job == "Executive Producer"}.sortedBy { it.name }
                            //Log.d("check getDetailMovie() inner flow","response data: ${response.data}")
                            val responseCopy = response.data.copy(
                                cast = cast,
                                executiveProducers = filterOnlyExecutiveProducers
                            )
                            //Log.d("check getDetailMovie() inner flow","response copied : $responseCopy")
                            emit(
                                Resource.Success(
                                    data = responseCopy
                                )
                            )
                        }.flowOn(Dispatchers.IO)
                    }
                }
            }

            is ApiResponse.Empty -> {
                //Log.d("check getTrendingNow() rp","empty")
                return flowOf(Resource.Error(message = "empty"))
            }

            is ApiResponse.Error -> {
                return flow {
                    emit(Resource.Error(message = response.errorMessage))
                }
            }
        }
    }

}