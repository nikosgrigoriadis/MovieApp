package com.example.movieapp.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (db == null) {
            db = Room.databaseBuilder( //create the db if it doesn't exist
                context.applicationContext,
                AppDatabase::class.java,
                "movies_db"
            )
                .fallbackToDestructiveMigration() //delete the db if change schema (change also to hilt AppModule)
                .build()
        }
        return db!!
    }
}