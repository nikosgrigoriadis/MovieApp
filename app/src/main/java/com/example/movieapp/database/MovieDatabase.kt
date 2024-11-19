package com.example.movieapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import java.util.concurrent.Flow.Subscriber

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    //add companion object
}

