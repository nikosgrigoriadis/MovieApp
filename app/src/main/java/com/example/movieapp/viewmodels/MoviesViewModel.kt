package com.example.movieapp.viewmodels


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
class MoviesViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

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

    fun markDataAsFetched() {
        _hasFetched.value = true
    }

    fun fetchMovies() {
        if (_hasFetched.value) return

        viewModelScope.launch {
            _isLoading.value = true
            val upcoming = MovieCategories("Upcoming", repository.getUpcomingMovies())
            val nowplaying = MovieCategories("Now Playing", repository.getNowPlayingMovies())
//            _categories.value = listOf(
//
//                MovieCategories(
//                    "Action",
//                    repository.getMoviesByGenre("28")
//                ),
//
//                MovieCategories(
//                    "Animation",
//                    repository.getMoviesByGenre("16")
//                ),
//
//                MovieCategories(
//                    "Drama",
//                    repository.getMoviesByGenre("18")
//                ),
//                MovieCategories(
//                    "Romance",
//                    repository.getMoviesByGenre("10749")
//                ),
//                MovieCategories(
//                    "Fantasy",
//                    repository.getMoviesByGenre("14")
//                ),
//                MovieCategories(
//                    "Comedy",
//                    repository.getMoviesByGenre("35")
//                ),
//                MovieCategories(
//                    "Adventure",
//                    repository.getMoviesByGenre("12")
//                ),
//                MovieCategories(
//                    "Thriller",
//                    repository.getMoviesByGenre("53")
//                ),
//                MovieCategories(
//                    "Horror",
//                    repository.getMoviesByGenre("27")
//                ),
//                MovieCategories(
//                    "Mystery",
//                    repository.getMoviesByGenre("9648")
//                ),
//                MovieCategories(
//                    "Crime",
//                    repository.getMoviesByGenre("80")
//                ),
//                MovieCategories(
//                    "Music",
//                    repository.getMoviesByGenre("10402")
//                ),
//                MovieCategories(
//                    "History",
//                    repository.getMoviesByGenre("36")
//                ),
//                MovieCategories(
//                    "War",
//                    repository.getMoviesByGenre("10752")
//                ),
//                MovieCategories(
//                    "Science Fiction",
//                    repository.getMoviesByGenre("878")
//                ),
//                MovieCategories(
//                    "Family",
//                    repository.getMoviesByGenre("10751")
//                ),
//                MovieCategories(
//                    "Documentary",
//                    repository.getMoviesByGenre("99")
//                ),
//                MovieCategories(
//                    "Western",
//                    repository.getMoviesByGenre("37")
//                ),
//                MovieCategories(
//                    "Most Popular",
//                    repository.getMostPopularMovies()
//                ),
//                MovieCategories(
//                    "Top Rated",
//                    repository.getTopRatedMovies()
//                )
//            )
            _upcomingMovies.value = upcoming.movies
            _nowPlayingMovies.value = nowplaying.movies
            _isLoading.value = false
        }
    }

    suspend fun getSpecificCategory(category: String) {
        val refreshMovies = when (category) {
            "Action" -> repository.getMoviesByGenre("28")
            "Animation" -> repository.getMoviesByGenre("16")
            "Drama" -> repository.getMoviesByGenre("18")
            "Romance" -> repository.getMoviesByGenre("10749")
            "Fantasy" -> repository.getMoviesByGenre("14")
            "Comedy" -> repository.getMoviesByGenre("35")
            "Adventure" -> repository.getMoviesByGenre("12")
            "Thriller" -> repository.getMoviesByGenre("53")
            "Horror" -> repository.getMoviesByGenre("27")
            "Mystery" -> repository.getMoviesByGenre("9648")
            "Crime" -> repository.getMoviesByGenre("80")
            "Music" -> repository.getMoviesByGenre("10402")
            "History" -> repository.getMoviesByGenre("36")
            "War" -> repository.getMoviesByGenre("10752")
            "Science Fiction" -> repository.getMoviesByGenre("878")
            "Family" -> repository.getMoviesByGenre("10751")
            "Documentary" -> repository.getMoviesByGenre("99")
            "Western" -> repository.getMoviesByGenre("37")
            "Most Popular" -> repository.getMostPopularMovies()
            "Top Rated" -> repository.getTopRatedMovies()
            else -> emptyList()
        }
        val refreshCategory = MovieCategories(category, refreshMovies)
        _categories.value = _categories.value.map {
            if (it.cat == category) refreshCategory else it
        }
    }

    suspend fun searchMovie(query: String): List<Movie> {
        return repository.searchMovies(query)
    }
}