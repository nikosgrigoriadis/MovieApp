package com.example.movieapp.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieCategories
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class MoviesViewModel : ViewModel() {

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
        val apikey = APIKEY
        viewModelScope.launch {   //CoroutineScope(Dispatchers.IO) -> viewModelScope
            _isLoading.value = true
            val upcoming = MovieCategories("Upcoming", RetroifitInstance.api.getUpcomingMovies(apikey).results)
            val nowplaying = MovieCategories("Now Playing", RetroifitInstance.api.getNowPlayingMovies(apikey).results)
            val tmdbCategory = listOf(

                        MovieCategories(
                            "Action",
                            RetroifitInstance.api.getActionMovies(apikey).results
                        ),
                        MovieCategories(
                            "Romance",
                            RetroifitInstance.api.getRomanceMovies(apikey).results
                        ),
                        MovieCategories(
                            "Most Popular",
                            RetroifitInstance.api.getPopularMovies(apikey).results
                        ),
                        MovieCategories(
                            "Top Rated",
                            RetroifitInstance.api.getTopRatedMovies(apikey).results
                        )
                    )
            _categories.value = tmdbCategory
            _upcomingMovies.value = upcoming.moviecoverchild
            _nowPlayingMovies.value = nowplaying.moviecoverchild
            _isLoading.value = false
        }
    }


    suspend fun searchMovie(query: String): List<Movie> {
        return try {
            RetroifitInstance.api.searchMovies(APIKEY, query).results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}