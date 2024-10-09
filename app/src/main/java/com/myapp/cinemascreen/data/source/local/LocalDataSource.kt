package com.myapp.cinemascreen.data.source.local

import android.content.Context
import android.util.Log
import com.myapp.cinemascreen.data.util.DataMapper
import com.myapp.cinemascreen.data.models.MovieListItem
import com.myapp.cinemascreen.data.models.MovieTVListItem
import com.myapp.cinemascreen.data.source.local.entity.CategoryEntity
import com.myapp.cinemascreen.data.source.local.entity.MovieListItemEntity
import com.myapp.cinemascreen.data.source.local.room.CinemaDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataSource @Inject constructor (
    @ApplicationContext private val context: Context,
    private val cinemaDao: CinemaDao
) {

    //insert trending movies
    suspend fun insertTrendingMovies(trendingMovieList: List<MovieListItem>){
        try {
            //insert category movietv in local = trending/popular/recent_search etc
            val trendingCategory = CategoryEntity(c_id = 1, name = "trending")
            cinemaDao.insertCategories(trendingCategory)

            val trendingMovieCategoryListEntity = trendingMovieList.map {
                    data ->
                MovieListItemEntity(id = data.id, poster_path = data.poster_path, media_type = data.media_type, category = trendingCategory)
            }
            cinemaDao.insertMovies(trendingMovieCategoryListEntity)
        }catch (e: Exception){
            Log.d("exception","exception in insertTrendingMovies ${e.printStackTrace()}")
        }
    }

    suspend fun getMoviesByCategoryId(id: Int) : List<MovieListItem>{
        try {
            val trendingMovieEntityList = cinemaDao.getMoviesByCategoryId(id)
            val trendingMovieItemList = trendingMovieEntityList.map {
                DataMapper.mapMovieItemEntityToItem(it)
            }
            return trendingMovieItemList
        }catch (e: Exception){
            Log.d("exception","exception in getMoviesByCategoryId ${e.printStackTrace()}")
            return listOf()
        }
    }

    //get recent search
    fun getMoviesByCategoryIdDesc(id:Int) : Flow<List<MovieListItem>> {
        try {
            val orderedMovieEntityList = cinemaDao.getMoviesByCategoryIdDesc(id)
            val orderedMovieItemList = orderedMovieEntityList.map {
                it.map {
                    DataMapper.mapMovieItemEntityToItem(it)
                }
            }
            return orderedMovieItemList
        }catch (e: Exception){
            Log.d("exception","exception in getMoviesByCategoryIdDesc ${e.printStackTrace()}")
            return flow { emptyList<List<MovieListItem>>() }
        }
    }

    suspend fun getCountMoviesByCategoryId(id:Int) : Int{
        return cinemaDao.getCountMoviesByCategoryId(id)
    }

    //insert recent search
    suspend fun insertRecentSearch(item: MovieTVListItem, dateTime: Long){
        try {
            //insert category movietv in local = trending/popular/recent_search etc
            val recentSearchCategory = CategoryEntity(c_id = 5, name = "recent_search")
            cinemaDao.insertCategories(recentSearchCategory)
            //10 is limit of items saved and displayed
            if(getCountMoviesByCategoryId(5)==10){
                //get oldest item added to db
                val oldestData = cinemaDao.getMovieByCategoryIdOldest(id = 5)
                //delete that oldest item from db
                cinemaDao.deleteMovieByIdAndCategory(mId = oldestData.id, cId = 5)
            }
            //insert item to db
            val recentSearchEntity = MovieListItemEntity(id = item.id, poster_path = item.poster_path, media_type = item.media_type, category = recentSearchCategory, time_added = dateTime)
            cinemaDao.insertMovies(listOf(recentSearchEntity))
        }catch (e: Exception){
            Log.d("exception","exception in insertRecentSearch in LocalDataSource ${e.printStackTrace()}")
        }
    }

    //delete all movies saved
    suspend fun deleteMovies(){
        try {
            cinemaDao.deleteMovies()
        }catch (e: Exception){
            Log.d("exception","exception in deleteMovies ${e.printStackTrace()}")
        }
    }

    //delete all movies categories saved
    suspend fun deleteMovieByCategory(categoryId: Int){
        try {
            cinemaDao.deleteMovieByCategory(categoryId)
        }catch (e: Exception){
            Log.d("exception","exception in deleteMovieCategories ${e.printStackTrace()}")
        }
    }
}