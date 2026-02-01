package com.example.movieapp.repositories

import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieDetailsResponse
import com.example.movieapp.data.Provider
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor() {

    var currentPage = 0

    private val api = RetroifitInstance.api

    suspend fun getUpcomingMovies(language: String): List<Movie> {
        return try {
            api.getUpcomingMovies(APIKEY, language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getNowPlayingMovies(language: String): List<Movie> {
        return try {
            api.getNowPlayingMovies(APIKEY, language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMoviesByGenre(genreId: String, language: String): List<Movie> {
        return try {
            api.getMoviesByGenreRetrofit(genreId, language = language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMostPopularMovies(language: String): List<Movie> {
        return try {
            api.getPopularMovies(APIKEY, language = language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun loadTopRatedNextPage(): Int {
        currentPage++
        return currentPage
    }

    suspend fun getTopRatedMovies(language: String): List<Movie> {
        return try {
            api.getTopRatedMovies(APIKEY, loadTopRatedNextPage(), language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchMovies(query: String, language: String): List<Movie> {
        return try {
            api.searchMovies(APIKEY, query, language).results
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMovieDetails(movieId: Int, language: String): MovieDetailsResponse? {
        return try {
            api.getMovieDetails(movieId, APIKEY, language)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWatchProviders(movieId: Int): List<Provider> {
        return try {
            val response = api.getWatchProviders(movieId, APIKEY)
            response.results["GR"]?.flatrate ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
