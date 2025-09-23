package com.example.movieapp.repositories

import com.example.movieapp.database.MovieSchedule
import com.example.movieapp.database.MovieScheduleDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ScheduledMovieRepository @Inject constructor(private val dao: MovieScheduleDao) {

    suspend fun addScheduledMovie(movie: MovieSchedule) {
        dao.insert(movie)
    }

    suspend fun removeScheduledMovie(movie: MovieSchedule) {
        dao.delete(movie)
    }

    suspend fun getScheduledMovies(): List<MovieSchedule> {
        return dao.getAllSchedules().first()
    }
}
