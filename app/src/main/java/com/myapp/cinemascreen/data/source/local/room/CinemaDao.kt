package com.myapp.cinemascreen.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myapp.cinemascreen.data.source.local.entity.CategoryEntity
import com.myapp.cinemascreen.data.source.local.entity.MovieListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CinemaDao {
    //insert movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieListItemEntity>)

    //insert categories
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: CategoryEntity)

    //get movie by category
    @Query("SELECT * FROM movies WHERE movies.c_id = :id")
    suspend fun getMoviesByCategoryId(id: Int) : List<MovieListItemEntity>

    //get count movie by category
    @Query("SELECT COUNT(*) FROM movies WHERE movies.c_id = :id")
    suspend fun getCountMoviesByCategoryId(id: Int) : Int

    //get movie tv ordered by time_added by category
    @Query("SELECT * FROM movies WHERE movies.c_id = :id ORDER BY time_added DESC")
    fun getMoviesByCategoryIdDesc(id: Int) : Flow<List<MovieListItemEntity>>

    //get oldest movie tv data added by category
    @Query("SELECT * FROM movies WHERE movies.c_id = :id ORDER BY time_added ASC LIMIT 1")
    suspend fun getMovieByCategoryIdOldest(id: Int) : MovieListItemEntity

    //delete a movie tv by a id and category
    @Query("DELETE FROM movies where movies.id = :mId AND movies.c_id = :cId")
    suspend fun deleteMovieByIdAndCategory(mId: Int, cId: Int)

    //delete all saved movies by a category
    @Query("DELETE FROM movies where movies.c_id = :id")
    suspend fun deleteMovieByCategory(id: Int)

    //delete all movies saved
    @Query("DELETE FROM movies")
    suspend fun deleteMovies()

    //delete all categories saved
    @Query("DELETE FROM categories")
    suspend fun deleteCategories()

}