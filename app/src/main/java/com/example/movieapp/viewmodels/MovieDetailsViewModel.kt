package com.example.movieapp.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieDetailsResponse
import com.example.movieapp.data.Provider
import com.example.movieapp.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val app: Application
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetails: StateFlow<MovieDetailsResponse?> = _movieDetails

    private val _watchProviders = MutableStateFlow<List<Provider>>(emptyList())
    val watchProviders: StateFlow<List<Provider>> = _watchProviders

    private val _tmdbLink = MutableStateFlow<String?>(null)
    val tmdbLink: StateFlow<String?> = _tmdbLink

    private val _language: MutableStateFlow<String>
    val language: StateFlow<String> get() = _language

    private val prefs = app.getSharedPreferences("lang_prefs", Context.MODE_PRIVATE)

    init {
        val savedLanguage = prefs.getString("selected_language", "en-US") ?: "en-US"
        _language = MutableStateFlow(savedLanguage)
    }

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _movieDetails.value = repository.getMovieDetails(movieId, _language.value)
            val providers = repository.getWatchProviders(movieId)
            _watchProviders.value = providers
            if (providers.isNotEmpty()) {
                // This is a bit of a hack, since the watch provider API response doesn't contain the movie link.
                // We are constructing the link manually.
                _tmdbLink.value = "https://www.themoviedb.org/movie/$movieId"
            }
        }
    }

    fun changeLanguage(movieId: Int, newLanguage: String) {
        _language.value = newLanguage
        prefs.edit().putString("selected_language", newLanguage).apply()
        loadMovie(movieId)
    }
}
