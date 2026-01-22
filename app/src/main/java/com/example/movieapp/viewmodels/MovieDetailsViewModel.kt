package com.example.movieapp.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.MovieDetailsResponse
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
        }
    }

    fun changeLanguage(movieId: Int, newLanguage: String) {
        _language.value = newLanguage
        prefs.edit().putString("selected_language", newLanguage).apply()
        loadMovie(movieId)
    }
}
