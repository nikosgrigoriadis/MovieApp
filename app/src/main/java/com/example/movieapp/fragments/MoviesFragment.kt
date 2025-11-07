package com.example.movieapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.activities.MainActivity
import com.example.movieapp.adapters.CarouselAdapter
import com.example.movieapp.adapters.CoversAdapter
import com.example.movieapp.adapters.MainAdapter
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.viewmodels.MoviesViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val APIKEY = "5e8009b02ba3ed667527c72cf4779a4d"

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private val viewModel: MoviesViewModel by activityViewModels()
    private var searchJob: Job? = null

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
        applysearchview()
        reloadclicked()
        checkNetworkConnection()
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchView.isShowing) {
            (activity as? MainActivity)?.hideBottomNav()
        }
    }
    
    fun refreshCategory(category: String) {
        lifecycleScope.launch {
            if (isNetworkAvailable(requireContext())) {
                viewModel.getSpecificCategory(category)
                viewModel.categories.collect { categoryList ->
                    binding.apply {
                        catrecyclerView.adapter = MainAdapter(categoryList, this@MoviesFragment)
                    }
                }
            }
            else {showNoInternetUI()}
        }
    }

    private fun searchForMovie(query: String) {
        lifecycleScope.launch {
            val results = viewModel.searchMovie(query)
            binding.apply {
                if (results.isNotEmpty()) {
                    noresultsanimation.visibility = View.GONE
                    noResultsText.visibility = View.GONE
                    searchrecyclerView.visibility = View.VISIBLE
                    searchrecyclerView.adapter = CoversAdapter(results, this@MoviesFragment)

                } else {
                    noresultsanimation.visibility = View.VISIBLE
                    noResultsText.text = "No results for \"$query\""
                    noResultsText.visibility = View.VISIBLE
                    searchrecyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun applysearchview() {

        binding.apply {
            // Connect SearchBar with SearchView
            searchView.setupWithSearchBar(searchBar)
            searchanimation.visibility = View.VISIBLE
            searchBar.setOnClickListener {
                searchView.show()
                searchView.setText("")
                (activity as? MainActivity)?.hideBottomNav()
            }

            val toolbar = searchView.findViewById<com.google.android.material.appbar.MaterialToolbar>(
                com.google.android.material.R.id.open_search_view_toolbar
            )

            val content_area = searchView.findViewById<View>(
                com.google.android.material.R.id.open_search_view_content_container
            )

            val clearButton = searchView.findViewById<ImageView>(
                com.google.android.material.R.id.open_search_view_clear_button
            )

            toolbar.setBackgroundColor(resources.getColor(R.color.search_toolbar, null))

            content_area.setBackgroundColor(resources.getColor(R.color.content_area, null))

            clearButton.setColorFilter(resources.getColor(R.color.white, null))

            searchView.editText.setTextColor(resources.getColor(R.color.light_black, null))

            searchView.editText.setHintTextColor(resources.getColor(R.color.lighter_black, null))

            toolbar.setNavigationIcon(R.drawable.arrow_back_ios)



            searchView.addTransitionListener { _, _, newState ->


                if (newState == com.google.android.material.search.SearchView.TransitionState.HIDDEN) {
                    (activity as? MainActivity)?.showBottomNav()
                    noresultsanimation.visibility = View.GONE
                    noResultsText.visibility = View.GONE
                    searchrecyclerView.adapter = CoversAdapter(emptyList(), this@MoviesFragment)
                }
            }

            //search by letter
            searchView.getEditText().addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString()

                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        delay(300)
                        if (query.isNotEmpty()) {
                            searchForMovie(query)
                            searchanimation.visibility = View.GONE
                        }
                        else {
                            searchrecyclerView.adapter = CoversAdapter(emptyList(), this@MoviesFragment)
                            searchanimation.visibility = View.VISIBLE
                            noresultsanimation.visibility = View.GONE
                            noResultsText.visibility = View.GONE
                        }
                    }
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            })
        }
    }

     fun FetchFromViewModel() {
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
                binding.apply {
                    catrecyclerView.adapter = MainAdapter(categoryList, this@MoviesFragment)
                }
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

    private fun reloadclicked() {
        binding.reload.setOnClickListener {
            checkNetworkConnection()
        }
    }

    private fun checkNetworkConnection() {
        if (!isNetworkAvailable(requireContext())) {
            showNoInternetUI()
        } else {
            binding.apply {
                mainFragment.visibility = View.VISIBLE
                (activity as? MainActivity)?.showBottomNav()
                nointernetanimation.visibility = View.GONE
                reload.visibility = View.GONE
            }
            FetchFromViewModel()
        }
    }

     fun showNoInternetUI() {
        binding.apply {
            loadingAnimationView.visibility = View.GONE
            nointernetanimation.visibility = View.VISIBLE
            reload.visibility = View.VISIBLE
            mainFragment.visibility = View.GONE
            searchBar.visibility = View.GONE
            (activity as? MainActivity)?.hideBottomNav()
        }
    }

     fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}