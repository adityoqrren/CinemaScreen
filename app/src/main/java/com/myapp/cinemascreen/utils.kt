package com.myapp.cinemascreen

import android.content.Context
import android.util.Log
import com.myapp.cinemascreen.data.Cast
import com.myapp.cinemascreen.data.CastData
import com.myapp.cinemascreen.data.CrewData
import com.myapp.cinemascreen.data.ImportantCrews
import com.myapp.cinemascreen.data.MovieData
import com.myapp.cinemascreen.data.MovieDetail
import com.myapp.cinemascreen.data.MovieListItem
import com.myapp.cinemascreen.data.TVData
import com.myapp.cinemascreen.data.TVListItem
import com.myapp.cinemascreen.data.TrendingMovieTVData
import com.google.gson.Gson

//now playing movies
fun generateMovieList(linkData: String, appContext: Context) : List<MovieListItem>{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(linkData)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, MovieData::class.java)
        return data.results
    }else{
        return emptyList()
    }
}

//trending movie and tv
fun generateMovieTVList(linkData: String, appContext: Context) : List<MovieListItem>{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(linkData)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, TrendingMovieTVData::class.java)
        //Log.d("generated movie list: ","${data.results}")
        return data.results
    }else{
        Log.d("generated movie list: ","datanya null")
        return emptyList()
    }
}

//airing now TV Shows
fun generateTVList(linkData: String, appContext: Context) : List<TVListItem>{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(linkData)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, TVData::class.java)
        //Log.d("generated movie list: ","${data.results}")
        return data.results
    }else{
        Log.d("generated movie list: ","datanya null")
        return emptyList()
    }
}

////now playing movies
//fun generateMovieList(appContext: Context) : List<MovieListItem>{
//    val gson = Gson()
//    val assetManager = appContext.assets
//    val inputStream = assetManager.open("datajson/movielist_nowplaying.json")
//    val jsonString = inputStream.bufferedReader().use { it.readText() }
//    if(jsonString.isNotEmpty()){
//        val data = gson.fromJson(jsonString, MovieData::class.java)
//        return data.results
//    }else{
//        return emptyList()
//    }
//}

//detail movie
fun generateMovieDetail(filename:String, appContext: Context) : MovieDetail{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(filename)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
//    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, MovieDetail::class.java)
        return data
//        Log.d("generated movie detail: ","${data.title} - ${data.production_companies}")
//    }else{
//        return null
//        Log.d("generated movie detail: ","datanya null")
//    }
}

fun getMovieYear(releaseDate : String) : String{
    return releaseDate.substring(0,4)
}

fun getDuration(runtime: Int) : String{
    if(runtime<60){
        return "$runtime minutes"
    }else{
        val hour = runtime/60
        val minutes = runtime%60
        val attributeHour = if(hour>1) "hours" else "hour"
        val attributeMinute = if(minutes>1) "minutes" else "minute"
        return "$hour $attributeHour $minutes $attributeMinute"
    }
}

//get top actors
fun generateTopActors(linkData: String, count: Int, appContext: Context) : List<Cast>{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(linkData)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, CastData::class.java)
        //Log.d("generated movie list: ","${data.results}")
        val filterOnlyActors = data.cast.filter { it.known_for_department == "Acting" && !it.character.isNullOrEmpty() }.take(count)
        return filterOnlyActors
    }else{
        Log.d("generated movie list: ","datanya null")
        return emptyList()
    }
}

//get director and screenplay
fun generateImportantCrews(linkData: String, appContext: Context) : ImportantCrews{
    val gson = Gson()
    val assetManager = appContext.assets
    val inputStream = assetManager.open(linkData)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    if(jsonString.isNotEmpty()){
        val data = gson.fromJson(jsonString, CrewData::class.java)
        //Log.d("generated movie list: ","${data.results}")
        val filterOnlyDirectors = data.crew.filter { it.known_for_department == "Directing" && it.job == "Director"}.map { it.name }
        val filterOnlyWriters = data.crew.filter { it.known_for_department == "Writing" && it.job == "Writer"}.map { it.name }
        val filterOnlyScreenplay = data.crew.filter { it.known_for_department == "Writing" && it.job == "Screenplay"}.map { it.name }
        return ImportantCrews(filterOnlyDirectors, filterOnlyWriters,filterOnlyScreenplay)
    }else{
        Log.d("generated movie list: ","datanya null")
        return ImportantCrews(emptyList(),null,null)
    }
}

