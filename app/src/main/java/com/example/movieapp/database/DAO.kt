package com.example.movieapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if the movie already exists
    suspend fun insert(movie: FavoriteMovieId)

    @Delete
    suspend fun delete(movie: FavoriteMovieId)

    @Query("DELETE FROM favorite_movies")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAllFavorites(): List<FavoriteMovieId>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}