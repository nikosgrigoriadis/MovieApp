package com.example.movieapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.MovieCategories
import com.example.movieapp.R
import com.example.movieapp.RetroifitInstance
import com.example.movieapp.adapters.CarouselAdapter
import com.example.movieapp.adapters.MainAdapter

import com.example.movieapp.databinding.FragmentMoviesBinding
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val APIKEY = "5e8009b02ba3ed667527c72cf4779a4d"

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
        checkNetworkConnection()
        binding.refreshdown.setOnRefreshListener {
            checkNetworkConnection()
            binding.refreshdown.isRefreshing = false
        }
    }

    fun fetchMovies() {

        val apikey = APIKEY
        val carouselRecyclerView = binding.carouselRecyclerView
        val catrecyclerView = binding.catrecyclerView

        CoroutineScope(Dispatchers.IO).launch { //api call in background
            val upcoming =  RetroifitInstance.api.getUpcomingMovies(apikey).results
            try {
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
                withContext(Dispatchers.Main) {  //return to main thread after api call to update UI
                    Log.d(
                        "TMDbResponseAction",
                        "Movies: ${RetroifitInstance.api.getActionMovies(apikey).results}"
                    )
                    LoadingScreen(false)
                    binding.upcomingText.visibility = View.VISIBLE
                    carouselRecyclerView.layoutManager = CarouselLayoutManager()
                    carouselRecyclerView.adapter = CarouselAdapter(upcoming)
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

    private fun checkNetworkConnection() {
        if (!isNetworkAvailable(requireContext())) {
            LoadingScreen(false)
            binding.nointernetext.visibility = View.VISIBLE
            binding.mainFragment.visibility = View.GONE
        } else {
            fetchMovies()
            binding.nointernetext.visibility = View.GONE
            binding.mainFragment.visibility = View.VISIBLE
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
