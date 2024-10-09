package com.myapp.cinemascreen.utils

import android.content.Context
import android.os.Build
import android.util.Log
import com.myapp.cinemascreen.data.models.Cast
import com.myapp.cinemascreen.data.models.CastData
import com.myapp.cinemascreen.data.models.CrewData
import com.myapp.cinemascreen.data.models.ImportantCrews
import com.myapp.cinemascreen.data.models.MovieData
import com.myapp.cinemascreen.data.models.MovieDetail
import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.TVData
import com.myapp.cinemascreen.data.models.TVListItem
import com.myapp.cinemascreen.data.models.TrendingMovieTVData
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun getMovieYear(releaseDate : String) : String{
    return releaseDate.substring(0,4)
}

fun getDate(inputDate: String) : String{
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val localDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val dateString = localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
        return dateString
    }else{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return date?.let { SimpleDateFormat("dd MMMM yyyy", java.util.Locale.getDefault()).format(it) }?: ""
    }
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



