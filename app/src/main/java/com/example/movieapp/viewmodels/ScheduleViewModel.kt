package com.example.movieapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.ScheduledMovieItem
import com.example.movieapp.database.MovieSchedule
import com.example.movieapp.fragments.APIKEY
import com.example.movieapp.network.RetroifitInstance.api
import com.example.movieapp.repositories.ScheduledMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel@Inject constructor (
    private val repository: ScheduledMovieRepository
) : ViewModel() {

    private val _scheduledMovies = MutableStateFlow<List<ScheduledMovieItem>>(emptyList())
    val scheduledMovies: StateFlow<List<ScheduledMovieItem>> = _scheduledMovies

    fun loadScheduled() {
        viewModelScope.launch {

            val schmoviesEntities = withContext(Dispatchers.IO) {repository.getScheduledMovies()}
            val itemList = mutableListOf<ScheduledMovieItem>()

            for (i in schmoviesEntities) {  //make it async without for loop
                val scmovie = withContext(Dispatchers.IO) {
                    api.getMovie(i.movieId, APIKEY,  language = Locale.getDefault().language)
                }
                itemList.add(ScheduledMovieItem(
                    scmovie.id,
                    scmovie.poster_path,
                    i.scheduledTime,
                    i.id,
                    scmovie.title)
                )
            }



            _scheduledMovies.value = itemList
        }

    }


    fun addScheduled(movie: MovieSchedule) {
        viewModelScope.launch {
            repository.addScheduledMovie(movie)
            loadScheduled()
        }
    }

    fun removeScheduled(movie: MovieSchedule) {
        viewModelScope.launch {
            repository.removeScheduledMovie(movie)
            loadScheduled()
        }
    }
}
