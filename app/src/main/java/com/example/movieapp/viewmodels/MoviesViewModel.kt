package com.example.movieapp.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieCategories
import com.example.movieapp.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MovieRepository, private val app: Application) : ViewModel() {

    private val _categories = MutableStateFlow<List<MovieCategories>>(emptyList())
    val categories: StateFlow<List<MovieCategories>> = _categories

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies

    private val _nowPlayingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlayingMovies: StateFlow<List<Movie>> = _nowPlayingMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasFetched = MutableStateFlow(false)
    val hasFetched: StateFlow<Boolean> = _hasFetched

    private val prefs = app.getSharedPreferences("lang_prefs", Context.MODE_PRIVATE)
    private val language: String
        get() = prefs.getString("selected_language", "en-US") ?: "en-US"

    fun markDataAsFetched() {
        _hasFetched.value = true
    }

    fun fetchMovies() {
        if (_hasFetched.value) return

        viewModelScope.launch {
            Log.d("MoviesViewModel", "Starting fetchMovies for language=$language")
            _isLoading.value = true
            val upcoming = MovieCategories("Upcoming", repository.getUpcomingMovies(language))
            val nowplaying = MovieCategories("Now Playing", repository.getNowPlayingMovies(language))
            _categories.value = listOf(

                MovieCategories(
                    "Action",
                    repository.getMoviesByGenre("28", language)
                ),

                MovieCategories(
                    "Animation",
                    repository.getMoviesByGenre("16", language)
                ),

                MovieCategories(
                    "Drama",
                    repository.getMoviesByGenre("18", language)
                ),
                MovieCategories(
                    "Romance",
                    repository.getMoviesByGenre("10749", language)
                ),
                MovieCategories(
                    "Fantasy",
                    repository.getMoviesByGenre("14", language)
                ),
                MovieCategories(
                    "Comedy",
                    repository.getMoviesByGenre("35", language)
                ),
                MovieCategories(
                    "Adventure",
                    repository.getMoviesByGenre("12", language)
                ),
                MovieCategories(
                    "Thriller",
                    repository.getMoviesByGenre("53", language)
                ),
                MovieCategories(
                    "Horror",
                    repository.getMoviesByGenre("27", language)
                ),
                MovieCategories(
                    "Mystery",
                    repository.getMoviesByGenre("9648", language)
                ),
                MovieCategories(
                    "Crime",
                    repository.getMoviesByGenre("80", language)
                ),
                MovieCategories(
                    "Music",
                    repository.getMoviesByGenre("10402", language)
                ),
                MovieCategories(
                    "History",
                    repository.getMoviesByGenre("36", language)
                ),
                MovieCategories(
                    "War",
                    repository.getMoviesByGenre("10752", language)
                ),
                MovieCategories(
                    "Science Fiction",
                    repository.getMoviesByGenre("878", language)
                ),
                MovieCategories(
                    "Family",
                    repository.getMoviesByGenre("10751", language)
                ),
                MovieCategories(
                    "Documentary",
                    repository.getMoviesByGenre("99", language)
                ),
                MovieCategories(
                    "Western",
                    repository.getMoviesByGenre("37", language)
                ),
                MovieCategories(
                    "Most Popular",
                    repository.getMostPopularMovies(language)
                ),
                MovieCategories(
                    "Top Rated",
                    repository.getTopRatedMovies(language)
                )
            )
            _upcomingMovies.value = upcoming.movies
            _nowPlayingMovies.value = nowplaying.movies
            _isLoading.value = false
            Log.d(
                "MoviesViewModel",
                "Finished fetchMovies: categories=${_categories.value.size}, upcoming=${_upcomingMovies.value.size}, nowPlaying=${_nowPlayingMovies.value.size}"
            )
        }
    }

    fun refreshMoviesInNewLanguage() {
        Log.d("MoviesViewModel", "Refreshing movies in new language=$language")
        _hasFetched.value = false
        fetchMovies()
    }

    suspend fun getSpecificCategory(category: String) {
        Log.d("MoviesViewModel", "Refreshing category=$category for language=$language")
        val refreshMovies = when (category) {
            "Action" -> repository.getMoviesByGenre("28", language)
            "Animation" -> repository.getMoviesByGenre("16", language)
            "Drama" -> repository.getMoviesByGenre("18", language)
            "Romance" -> repository.getMoviesByGenre("10749", language)
            "Fantasy" -> repository.getMoviesByGenre("14", language)
            "Comedy" -> repository.getMoviesByGenre("35", language)
            "Adventure" -> repository.getMoviesByGenre("12", language)
            "Thriller" -> repository.getMoviesByGenre("53", language)
            "Horror" -> repository.getMoviesByGenre("27", language)
            "Mystery" -> repository.getMoviesByGenre("9648", language)
            "Crime" -> repository.getMoviesByGenre("80", language)
            "Music" -> repository.getMoviesByGenre("10402", language)
            "History" -> repository.getMoviesByGenre("36", language)
            "War" -> repository.getMoviesByGenre("10752", language)
            "Science Fiction" -> repository.getMoviesByGenre("878", language)
            "Family" -> repository.getMoviesByGenre("10751", language)
            "Documentary" -> repository.getMoviesByGenre("99", language)
            "Western" -> repository.getMoviesByGenre("37", language)
            "Most Popular" -> repository.getMostPopularMovies(language)
            "Top Rated" -> repository.getTopRatedMovies(language)
            else -> emptyList()
        }
        val refreshCategory = MovieCategories(category, refreshMovies)
        _categories.value = _categories.value.map {
            if (it.cat == category) refreshCategory else it
        }
        Log.d("MoviesViewModel", "Category refresh completed category=$category size=${refreshMovies.size}")
    }

    suspend fun searchMovie(query: String): List<Movie> {
        Log.d("MoviesViewModel", "Searching movies query=$query language=$language")
        return repository.searchMovies(query, language)
    }
}
