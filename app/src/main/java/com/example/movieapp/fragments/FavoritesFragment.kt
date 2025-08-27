package com.example.movieapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.adapters.FavoritesAdapter
import com.example.movieapp.data.Movie
import com.example.movieapp.database.DatabaseProvider
import com.example.movieapp.databinding.FragmentFavoritesBinding
import com.example.movieapp.network.RetroifitInstance.api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadfavoritemovies()
    }

    private fun loadfavoritemovies() {
        val dao = DatabaseProvider.getDatabase(requireContext()).favoriteMovieDao()
        binding.loadingAnimationView.visibility = View.VISIBLE
        lifecycleScope.launch {
            val favoriteIds = withContext(Dispatchers.IO) { //withContext because we take results
                dao.getAllFavorites()
            }
            val favmovies = mutableListOf<Movie>()

            for (fav in favoriteIds) {
                val movie = withContext(Dispatchers.IO) {
                    api.getMovie(fav.id, APIKEY)
                }
                Log.d("Movie", movie.toString())
                Log.d("Movie", "Hello")
                favmovies.add(movie)
            }
            binding.apply {
                if (favmovies.isEmpty()) {
                    brokenHeartAnimationView.visibility = View.VISIBLE
                    nofavoritestextView.visibility = View.VISIBLE
                    favoritesrecyclerView.visibility = View.GONE
                    favorites.visibility = View.GONE
                } else {
                    brokenHeartAnimationView.visibility = View.GONE
                    nofavoritestextView.visibility = View.GONE
                    favoritesrecyclerView.visibility = View.VISIBLE
                    favorites.visibility = View.VISIBLE

                    adapter = FavoritesAdapter(favmovies, this@FavoritesFragment)
                    favoritesrecyclerView.adapter = adapter
                }
                binding.loadingAnimationView.visibility = View.GONE
            }
        }
    }


}