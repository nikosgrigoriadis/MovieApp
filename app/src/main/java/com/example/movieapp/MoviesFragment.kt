package com.example.movieapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentMoviesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MoviesFragment: Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMovies()
    }

    private fun fetchMovies() {
        val apikey = "5e8009b02ba3ed667527c72cf4779a4d"
        val catrecyclerView = binding.catrecyclerView

        CoroutineScope(Dispatchers.IO).launch { //api call in background

            try {

                val tmdbCategory = listOf(
                    MovieCategories("Most Popular",RetroifitInstance.api.getPopularMovies(apikey).results),
                    MovieCategories("Now Playing",RetroifitInstance.api.getNowPlayingMovies(apikey).results)
                )
                withContext(Dispatchers.Main) {
                    catrecyclerView.adapter = MainAdapter(tmdbCategory)
                }
            } catch (e: Exception) {
                Log.e("TMDbError", "Error: ${e.message}")
            }
        }
    }
}