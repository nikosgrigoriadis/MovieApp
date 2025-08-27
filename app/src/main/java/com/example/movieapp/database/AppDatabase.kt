package com.example.movieapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovieId::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}