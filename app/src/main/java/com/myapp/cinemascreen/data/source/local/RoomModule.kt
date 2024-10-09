package com.myapp.cinemascreen.data.source.local

import android.content.Context
import androidx.room.Room
import com.myapp.cinemascreen.data.source.local.room.CinemaDao
import com.myapp.cinemascreen.data.source.local.room.CinemaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule{

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : CinemaDatabase {
        return Room.databaseBuilder(
            context,
            CinemaDatabase::class.java, "cinemascreen.db"
        ).fallbackToDestructiveMigration()
            //.openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(cinemaDatabase: CinemaDatabase) : CinemaDao{
        return cinemaDatabase.cinemaDao()
    }
}