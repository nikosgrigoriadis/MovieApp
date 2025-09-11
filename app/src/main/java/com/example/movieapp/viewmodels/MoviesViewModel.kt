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

    fun resetFetchFlag() {
        _hasFetched.value = false
    }

    fun fetchMovies() {
        if (_hasFetched.value) return

        viewModelScope.launch {   //CoroutineScope(Dispatchers.IO) -> viewModelScope
            _isLoading.value = true
            val upcoming = MovieCategories("Upcoming", repository.getUpcomingMovies())
            val nowplaying = MovieCategories("Now Playing", repository.getNowPlayingMovies())
            _categories.value = listOf(

                MovieCategories(
                    "Action",
                    repository.getActionMovies()
                ),
                MovieCategories(
                    "Romance",
                    repository.getRomanceMovies()
                ),
                MovieCategories(
                    "Most Popular",
                    repository.getMostPopularMovies()
                ),
                MovieCategories(
                    "Top Rated",
                    repository.getTopRatedMovies()
                )
            )
            _upcomingMovies.value = upcoming.movies
            _nowPlayingMovies.value = nowplaying.movies
            _isLoading.value = false
        }
    }

    suspend fun getSpecificCategory(category: String) {
        val refreshMovies = when (category) {
            "Action" -> repository.getActionMovies()
            "Romance" -> repository.getRomanceMovies()
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