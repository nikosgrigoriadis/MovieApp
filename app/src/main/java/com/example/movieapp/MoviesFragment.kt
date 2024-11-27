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


class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMovies()
    }

    fun fetchMovies() {  //να το βαλω σε view model
        LoadingScreen(true)
        val apikey = "5e8009b02ba3ed667527c72cf4779a4d"
        val catrecyclerView = binding.catrecyclerView
        CoroutineScope(Dispatchers.IO).launch { //api call in background
            val response = RetroifitInstance.api.getNowPlayingMovies(apikey).results
            try {
                val tmdbCategory = listOf(
                    MovieCategories(
                        "Now Playing",
                        response
                    ),
                    MovieCategories(
                        "Upcoming",
                        RetroifitInstance.api.getUpcomingMovies(apikey).results
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
                withContext(Dispatchers.Main) {
                    LoadingScreen(false)
                    catrecyclerView.adapter = MainAdapter(tmdbCategory)
                }
            } catch (e: Exception) {
                Log.e("TMDbError", "Error: ${e.message}")
            }
        }
    }

    private fun LoadingScreen(loading: Boolean) {
        if (loading) {
            binding.loadingAnimationView.visibility = View.VISIBLE
            binding.catrecyclerView.visibility = View.GONE
        } else {
            binding.loadingAnimationView.visibility = View.GONE
            binding.catrecyclerView.visibility = View.VISIBLE
        }

    }
}