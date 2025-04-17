package com.example.movieapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.adapters.CarouselAdapter
import com.example.movieapp.adapters.MainAdapter
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.viewmodels.MoviesViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.launch

const val APIKEY = "5e8009b02ba3ed667527c72cf4779a4d"

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by viewModels()
    private var alreadyfetch = false

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
            alreadyfetch = false
            checkNetworkConnection()
            binding.refreshdown.isRefreshing = false
        }
    }

    fun FetchFromViewModel() {
        binding.upcomingText.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.upcomingMovies.collect { upcomingInsert  ->
                binding.carouselRecyclerView.layoutManager = CarouselLayoutManager()
                binding.carouselRecyclerView.adapter = CarouselAdapter(upcomingInsert)
            }
        }
        lifecycleScope.launch {
            viewModel.categories.collect { categoryList  ->
                binding.catrecyclerView.adapter = MainAdapter(categoryList)
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                LoadingScreen(isLoading)
            }
        }
        if (alreadyfetch) return
        viewModel.fetchMovies()
        alreadyfetch = true
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
            binding.nointernetext.visibility = View.VISIBLE
            binding.mainFragment.visibility = View.GONE
        } else {
            FetchFromViewModel()
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