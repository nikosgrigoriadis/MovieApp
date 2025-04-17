package com.example.movieapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.Movie
import com.example.movieapp.MovieCategories
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MoviesViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<MovieCategories>>(emptyList())
    val categories: StateFlow<List<MovieCategories>> = _categories

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading




    fun fetchMovies() {
        val apikey = APIKEY
        viewModelScope.launch {   //CoroutineScope(Dispatchers.IO) -> viewModelScope
            _isLoading.value = true
            val upcoming = MovieCategories("Upcoming", RetroifitInstance.api.getUpcomingMovies(apikey).results)

            val tmdbCategory = listOf(
                        MovieCategories(
                            "Now Playing",
                            RetroifitInstance.api.getNowPlayingMovies(apikey).results
                        ),
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
            _isLoading.value = false
        }
    }
}
