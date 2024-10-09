package com.myapp.cinemascreen.data.source.remote

import com.myapp.cinemascreen.data.models.TVDetail
import com.myapp.cinemascreen.BuildConfig
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.data.source.remote.response.MediaListResponse
import com.myapp.cinemascreen.data.source.remote.response.MovieCreditResponse
import com.myapp.cinemascreen.data.source.remote.response.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //get list trending movies
    //get popular right now
     //1. streaming
     //2. on tv
     //3. for rent
     //4. on cinema
    //get now in cinemas
    //get airing now

    //trending Movies and TV
    //https://api.themoviedb.org/3/trending/all/day?language=en-US
    @GET("trending/all/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("language") language:String = "en-US"
    ) : MovieListResponse

    //popular Movies and TV
    //https://api.themoviedb.org/3/discover/movie
    @GET("discover/{media_type}")
    suspend fun getPopularMovieTV(
        @Path("media_type") mediaType: String,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("watch_region") watchRegion:String = "US",
        @Query("language") language:String = "en-US",
        @Query("sort_by") sortBy:String = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_watch_monetization_types") watchMonetizationTypes:String = "",
        @Query("with_release_type") releaseType: String = "",
    ) : MediaListResponse

    //now in cinemas movies
    //https://api.themoviedb.org/3/movie/now_playing?api_key=xxxxx&language=en-US&page=1&adult=false
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("region") watchRegion:String = "id",
        @Query("language") language:String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
    ) : MediaListResponse

    //airing now tv
    //https://api.themoviedb.org/3/tv/airing_today?language=en-US&first_air_date.gte=2024&include_adult=false&watch_region=id, flatrate&sort_by=popularity.desc
    @GET("tv/airing_today")
    suspend fun getAiringTodayTV(
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("watch_region") watchRegion:String = "id,flatrate",
        @Query("language") language:String = "en-US",
        @Query("sort_by") sortBy:String = "popularity.desc",
        @Query("first_air_date.gte") firstAirDateGTE:String = "2024",
        @Query("include_adult") includeAdult: Boolean = false,
    ) : MediaListResponse

    //detail Movie
    //https://api.themoviedb.org/3/movie/447277?language=en-US&append_to_response=videos
    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId : Int,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("language") language:String = "en-US",
        @Query("append_to_response") appendToResponse: String = "videos"
    ) : MovieDetail

    //detail TV
    @GET("tv/{tv_id}")
    suspend fun getDetailTV(
        @Path("tv_id") tvId : Int,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("language") language:String = "en-US",
        @Query("append_to_response") appendToResponse: String = "videos"
    ) : TVDetail

    //credits
    //https://api.themoviedb.org/3/movie/447277/credits?language=en-US
    @GET("{media_type}/{movie_id}/credits")
    suspend fun getCredit(
        @Path("media_type") media_type : String,
        @Path("movie_id") movie_id : Int,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("language") language:String = "en-US",
    ) : MovieCreditResponse

    //search
    //https://api.themoviedb.org/3/search/movie?query=spiderman&language=en-US&page=1&sort_by=popularity.desc
    @GET("search/{media_type}")
    suspend fun searchMovieTV(
        @Path("media_type") media_type : String,
        @Query("api_key") apiKey:String = BuildConfig.API_KEY,
        @Query("query") query:String,
        @Query("page") page:Int,
        @Query("include_adult") includeAdult:Boolean = false,
        @Query("language") language:String = "en-US",
        //@Query("sort_by") sort_by:String = "popularity.desc"
    ) : MediaListResponse
}