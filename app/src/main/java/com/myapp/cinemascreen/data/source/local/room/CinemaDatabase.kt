package com.myapp.cinemascreen.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.cinemascreen.data.source.local.entity.CategoryEntity
import com.myapp.cinemascreen.data.source.local.entity.MovieListItemEntity

@Database(entities = [MovieListItemEntity::class, CategoryEntity::class], version = 1, exportSchema = false)
abstract class CinemaDatabase : RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao
}