package com.example.movieapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//define functions that access the database

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if there is a conflict(same id), replace the old movie with the new one
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movie_data_table")
    suspend fun getMovies(movie: MovieEntity): List<MovieEntity>
}