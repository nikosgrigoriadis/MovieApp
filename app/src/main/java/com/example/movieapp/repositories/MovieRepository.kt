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

    suspend fun getActionMovies(): List<Movie> {
        return try {
            api.getActionMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getAnimationMovies(): List<Movie> {
        return try {
            api.getAnimationMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getDramaMovies(): List<Movie> {
        return try {
            api.getDramaMovies(APIKEY).results
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

    suspend fun getFantasyMovies(): List<Movie> {
        return try {
            api.getFantasyMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAdventureMovies(): List<Movie> {
        return try {
            api.getAdventureMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getThrillerMovies(): List<Movie> {
        return try {
            api.getThrillerMovies(APIKEY).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHorrorMovies(): List<Movie> {
        return try {
            api.getHorrorMovies(APIKEY).results
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