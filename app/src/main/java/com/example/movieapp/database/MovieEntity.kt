package com.example.movieapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_data_table")
data class MovieEntity (

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int,

    @ColumnInfo(name = "movie_name")
    val name: String,

    @ColumnInfo(name = "movie_overview")
    val overview: String,

    @ColumnInfo(name = "movie_coverPath")
    val coverPath: String?

)



