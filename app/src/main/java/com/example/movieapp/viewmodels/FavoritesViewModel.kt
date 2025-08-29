package com.example.movieapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.Movie
import com.example.movieapp.database.FavoriteMovieDao
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance.api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val dao: FavoriteMovieDao): ViewModel() {

    private val _favorites = MutableStateFlow<List<Movie>>(emptyList())
    val favorites: StateFlow<List<Movie>> = _favorites

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

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
            _loading.value = true

            //withContext because we take results
            val favoriteIds = withContext(Dispatchers.IO) { dao.getAllFavorites()}

            val favmovies = mutableListOf<Movie>()

            for (fav in favoriteIds) {
                val movie = withContext(Dispatchers.IO) {
                    api.getMovie(fav.id, APIKEY)
                }
                favmovies.add(movie)

            }
            _favorites.value = favmovies
            _loading.value = false
        }
    }
}