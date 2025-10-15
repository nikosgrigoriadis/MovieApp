package com.example.movieapp.repositories

import com.example.movieapp.data.Movie
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor() {

    var currentPage = 0

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

    suspend fun getMoviesByGenre(genreId: String): List<Movie> {
        return try {
            api.getMoviesByGenreRetrofit(genreId).results
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

    private fun loadTopRatedNextPage(): Int {
        currentPage++
        return currentPage
    }

    suspend fun getTopRatedMovies(): List<Movie> {
        return try {
            api.getTopRatedMovies(APIKEY, loadTopRatedNextPage()).results
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