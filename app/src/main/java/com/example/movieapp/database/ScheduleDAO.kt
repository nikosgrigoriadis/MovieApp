package com.example.movieapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: MovieSchedule)

    @Query("SELECT * FROM movie_schedule ORDER BY scheduledTime ASC")
    fun getAllSchedules(): Flow<List<MovieSchedule>>

    @Delete
    suspend fun delete(schedule: MovieSchedule)
}
