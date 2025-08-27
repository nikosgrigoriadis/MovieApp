package com.example.movieapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import androidx.fragment.app.Fragment
import com.example.movieapp.adapters.CarouselAdapter
import com.example.movieapp.adapters.CoversAdapter
import com.example.movieapp.adapters.MainAdapter
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.viewmodels.MoviesViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.launch

const val APIKEY = "5e8009b02ba3ed667527c72cf4779a4d"

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by activityViewModels()

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

        binding.apply {
            // Connect SearchBar with SearchView
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { view, actionId, event ->
                val query = searchView.text.toString()
                if (query.isNotEmpty()) {
                    searchForMovie(query)
                }
                true
            }
        }

        checkNetworkConnection()
        setupRefreshListener()
    }

    private fun searchForMovie(query: String) {
        lifecycleScope.launch {
            val results = viewModel.searchMovie(query)
            binding.apply {
                if (results.isNotEmpty()) {
                    noresultsanimation.visibility = View.GONE
                    searchrecyclerView.visibility = View.VISIBLE
                    searchrecyclerView.adapter = CoversAdapter(results, this@MoviesFragment)

                } else {
                    noresultsanimation.visibility = View.VISIBLE
                    searchrecyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun FetchFromViewModel() {
        lifecycleScope.launch {
            viewModel.upcomingMovies.collect { upcomingInsert ->
                binding.apply {
                    upcarouselRecyclerView.layoutManager = CarouselLayoutManager()
                    upcarouselRecyclerView.adapter = CarouselAdapter(upcomingInsert)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.nowPlayingMovies.collect { nowplayingInsert ->
                binding.apply {
                    npcarouselRecyclerView.layoutManager = CarouselLayoutManager()
                    npcarouselRecyclerView.adapter = CarouselAdapter(nowplayingInsert)
                }
            }
        }


        lifecycleScope.launch {
            viewModel.categories.collect { categoryList ->
                binding.apply { catrecyclerView.adapter = MainAdapter(categoryList,this@MoviesFragment) }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                LoadingScreen(isLoading)
            }
        }
        lifecycleScope.launch {
            viewModel.hasFetched.collect { fetched ->
                if (!fetched && isNetworkAvailable(requireContext())) {
                    viewModel.fetchMovies()
                    viewModel.markDataAsFetched()
                }
            }
        }
    }

    private fun LoadingScreen(loading: Boolean) {
        if (loading) {
            binding.apply {
                loadingAnimationView.visibility = View.VISIBLE
                catrecyclerView.visibility = View.GONE
                upcarouselRecyclerView.visibility = View.GONE
                npcarouselRecyclerView.visibility = View.GONE
                searchBar.visibility = View.GONE
                upcomingText.visibility = View.GONE
                nowplayingText.visibility = View.GONE
            }
        } else {
            binding.apply {
                loadingAnimationView.visibility = View.GONE
                nointernetanimation.visibility = View.GONE
                upcarouselRecyclerView.visibility = View.VISIBLE
                npcarouselRecyclerView.visibility = View.VISIBLE
                catrecyclerView.visibility = View.VISIBLE
                searchBar.visibility = View.VISIBLE
                upcomingText.visibility = View.VISIBLE
                nowplayingText.visibility = View.VISIBLE
            }
        }
    }

    private fun checkNetworkConnection() {
        if (!isNetworkAvailable(requireContext())) {
            showNoInternetUI()
        } else {
            binding.apply {
                mainFragment.visibility = View.VISIBLE
                nointernetanimation.visibility = View.GONE
            }
            FetchFromViewModel()
        }
    }

    private fun showNoInternetUI() {
        binding.apply {
            loadingAnimationView.visibility = View.GONE
            nointernetanimation.visibility = View.VISIBLE
            mainFragment.visibility = View.GONE
            searchBar.visibility = View.GONE
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

    private fun setupRefreshListener() {
        binding.refreshdown.setOnRefreshListener {
            binding.apply {
                if (isNetworkAvailable(requireContext())) {
                    viewModel.resetFetchFlag()
                    checkNetworkConnection()
                } else {
                    showNoInternetUI()
                }
                refreshdown.isRefreshing = false
            }
        }
    }
}