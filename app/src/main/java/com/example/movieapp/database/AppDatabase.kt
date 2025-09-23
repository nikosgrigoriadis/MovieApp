package com.example.movieapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovieId::class, MovieSchedule::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun movieScheduleDao(): MovieScheduleDao

}