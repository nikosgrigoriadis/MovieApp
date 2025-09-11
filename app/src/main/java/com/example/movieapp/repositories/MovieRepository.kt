package com.example.movieapp.repositories

import com.example.movieapp.data.Movie
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor() {

    private val api = RetroifitInstance.api

    suspend fun getUpcomingMovies(): List<Movie> {
        return try {
            api.getUpcomingMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getNowPlayingMovies(): List<Movie> {
        return try {
            api.getNowPlayingMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getActionMovies(): List<Movie> {
        return try {
            api.getActionMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRomanceMovies(): List<Movie> {
        return try {
            api.getRomanceMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMostPopularMovies(): List<Movie> {
        return try {
            api.getPopularMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        return try {
            api.getTopRatedMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return try {
            api.searchMovies(APIKEY, query).results
        } catch (e: Exception) {
            emptyList()
        }
    }
}