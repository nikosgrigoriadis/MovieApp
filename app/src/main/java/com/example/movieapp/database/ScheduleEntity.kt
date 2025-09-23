package com.example.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_schedule")
data class MovieSchedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val scheduledTime: Long
)
