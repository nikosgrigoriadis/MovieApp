package com.example.movieapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.database.FavoriteMovieDao
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance.api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val dao: FavoriteMovieDao): ViewModel() {

    private val _favorites = MutableStateFlow<List<Movie>>(emptyList())
    val favorites: StateFlow<List<Movie>> = _favorites

    private val _hasFetched = MutableStateFlow(false)
    val hasFetched: StateFlow<Boolean> = _hasFetched

    fun markDataAsFetched() {
        _hasFetched.value = true
    }

    fun resetFetchFlag() {
        _hasFetched.value = false
    }

     fun loadfavoritemovies() {

         if (_hasFetched.value) return

        viewModelScope.launch {

            val favoriteIds = withContext(Dispatchers.IO) { dao.getAllFavorites() }
            val language = Locale.getDefault().language

            val favMovies = favoriteIds.map { favId ->
                async(Dispatchers.IO) {
                    api.getMovie(favId.id, APIKEY, language)
                }
            }.awaitAll()

            _favorites.value = favMovies
        }
    }
}