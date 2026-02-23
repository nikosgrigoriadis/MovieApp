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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val app: Application
) : ViewModel() {

    private val _categories = MutableStateFlow<List<MovieCategories>>(emptyList())
    val categories: StateFlow<List<MovieCategories>> = _categories

    private val _selectedCategories = MutableStateFlow<Set<String>?>(null)
    val selectedCategories: StateFlow<Set<String>?> = _selectedCategories

    val filteredCategories: StateFlow<List<MovieCategories>> =
        combine(_categories, _selectedCategories) { categories, selected ->
            val selectedSet = selected ?: categoryOrder.toSet()
            categories.filter { it.cat in selectedSet }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _upcomingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val upcomingMovies: StateFlow<List<Movie>> = _upcomingMovies

    private val _nowPlayingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlayingMovies: StateFlow<List<Movie>> = _nowPlayingMovies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasFetched = MutableStateFlow(false)
    val hasFetched: StateFlow<Boolean> = _hasFetched

    private val _availableCategoryNames = MutableStateFlow(categoryOrder)
    val availableCategoryNames: StateFlow<List<String>> = _availableCategoryNames

    private val loadedCategoryMap = mutableMapOf<String, MovieCategories>()

    private val prefs = app.getSharedPreferences("lang_prefs", Context.MODE_PRIVATE)
    private val language: String
        get() = prefs.getString("selected_language", "en-US") ?: "en-US"

    private val categoriesPrefs =
        app.getSharedPreferences("category_filter_prefs", Context.MODE_PRIVATE)

    fun markDataAsFetched() {
        _hasFetched.value = true
    }

    fun fetchMovies() {
        if (_hasFetched.value) return

        viewModelScope.launch {
            Log.d("MoviesViewModel", "Starting fetchMovies for language=$language")
            _isLoading.value = true

            val upcoming = MovieCategories("Upcoming", repository.getUpcomingMovies(language))
            val nowPlaying = MovieCategories("Now Playing", repository.getNowPlayingMovies(language))

            val selectedSet = initializeSelectedCategories()
            loadedCategoryMap.clear()
            for (category in selectedSet) {
                loadedCategoryMap[category] = MovieCategories(category, loadCategoryMovies(category))
            }
            publishCategories()

            _upcomingMovies.value = upcoming.movies
            _nowPlayingMovies.value = nowPlaying.movies
            _isLoading.value = false
            Log.d(
                "MoviesViewModel",
                "Finished fetchMovies: categories=${_categories.value.size}, upcoming=${_upcomingMovies.value.size}, nowPlaying=${_nowPlayingMovies.value.size}"
            )
        }
    }

    fun refreshMoviesInNewLanguage() {
        Log.d("MoviesViewModel", "Refreshing movies in new language=$language")
        loadedCategoryMap.clear()
        _hasFetched.value = false
        fetchMovies()
    }

    suspend fun getSpecificCategory(category: String) {
        if (!(_selectedCategories.value ?: emptySet()).contains(category)) {
            return
        }

        Log.d("MoviesViewModel", "Refreshing category=$category for language=$language")
        val refreshCategory = MovieCategories(category, loadCategoryMovies(category))
        loadedCategoryMap[category] = refreshCategory
        publishCategories()
        Log.d(
            "MoviesViewModel",
            "Category refresh completed category=$category size=${refreshCategory.movies.size}"
        )
    }

    suspend fun searchMovie(query: String): List<Movie> {
        Log.d("MoviesViewModel", "Searching movies query=$query language=$language")
        return repository.searchMovies(query, language)
    }

    fun setCategoryChecked(category: String, isChecked: Boolean): Boolean {
        val currentSelection = _selectedCategories.value ?: categoryOrder.toSet()

        if (!isChecked && currentSelection.size == 1 && currentSelection.contains(category)) {
            return false
        }

        val updatedSelection = if (isChecked) {
            currentSelection + category
        } else {
            currentSelection - category
        }

        _selectedCategories.value = updatedSelection
        saveHiddenCategories(updatedSelection)

        if (isChecked && !loadedCategoryMap.containsKey(category)) {
            viewModelScope.launch {
                loadedCategoryMap[category] = MovieCategories(category, loadCategoryMovies(category))
                publishCategories()
            }
        } else {
            publishCategories()
        }

        return true
    }

    private fun initializeSelectedCategories(): Set<String> {
        val availableCategories = categoryOrder.toSet()
        val hiddenCategories = categoriesPrefs
            .getStringSet(KEY_HIDDEN_CATEGORIES, emptySet())
            ?.intersect(availableCategories)
            ?: emptySet()

        var selectedCategories = availableCategories - hiddenCategories
        if (selectedCategories.isEmpty()) {
            selectedCategories = setOf(categoryOrder.first())
            saveHiddenCategories(selectedCategories)
        }

        _selectedCategories.value = selectedCategories
        return selectedCategories
    }

    private fun publishCategories() {
        val selectedSet = _selectedCategories.value ?: categoryOrder.toSet()
        _categories.value = categoryOrder
            .filter { it in selectedSet }
            .mapNotNull { loadedCategoryMap[it] }
    }

    private suspend fun loadCategoryMovies(category: String): List<Movie> {
        return when (category) {
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
    }

    private fun saveHiddenCategories(selectedCategories: Set<String>) {
        val hiddenCategories = categoryOrder.toSet() - selectedCategories
        categoriesPrefs.edit().putStringSet(KEY_HIDDEN_CATEGORIES, hiddenCategories).apply()
    }

    companion object {
        private const val KEY_HIDDEN_CATEGORIES = "hidden_categories"

        private val categoryOrder = listOf(
            "Action",
            "Animation",
            "Drama",
            "Romance",
            "Fantasy",
            "Comedy",
            "Adventure",
            "Thriller",
            "Horror",
            "Mystery",
            "Crime",
            "Music",
            "History",
            "War",
            "Science Fiction",
            "Family",
            "Documentary",
            "Western",
            "Most Popular",
            "Top Rated"
        )
    }
}