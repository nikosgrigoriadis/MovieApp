package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.adapters.FavoritesAdapter
import com.example.movieapp.data.Movie
import com.example.movieapp.database.DatabaseProvider
import com.example.movieapp.databinding.FragmentFavoritesBinding
import com.example.movieapp.viewmodels.FavoritesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoritesAdapter

    private val viewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchfavoritesviewmodel()
        binclicked()
    }

    private fun binclicked() {
        binding.removeallfav.setOnClickListener {
            val dao = DatabaseProvider.getDatabase(requireContext()).favoriteMovieDao()
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Confirm Deletion")
                .setMessage("This action will remove all your favorites. Are you sure?")
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.resetFetchFlag()
                    lifecycleScope.launch {
                        dao.deleteAll()
                        not_exist_favorites()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun fetchfavoritesviewmodel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadfavoritemovies()
            viewModel.favorites.collect { favmovies ->
                if (favmovies.isEmpty()) {
                    withContext(Dispatchers.Main) { not_exist_favorites() }
                } else {
                    withContext(Dispatchers.Main) {
                        exist_favorites(favmovies) }
                }
                viewModel.markDataAsFetched()
            }
        }
    }

    private fun not_exist_favorites() {
        binding.apply {
            favoritesrecyclerView.visibility = View.GONE
            favorites.visibility = View.GONE
            removeallfav.visibility = View.GONE
            brokenHeartAnimationView.visibility = View.VISIBLE
            nofavoritestextView.visibility = View.VISIBLE
        }
    }

    private fun exist_favorites(favmovies: List<Movie>) {
        binding.apply {
            brokenHeartAnimationView.visibility = View.GONE
            nofavoritestextView.visibility = View.GONE
            favoritesrecyclerView.visibility = View.VISIBLE
            favorites.visibility = View.VISIBLE
            removeallfav.visibility = View.VISIBLE
            adapter = FavoritesAdapter(favmovies, this@FavoritesFragment)
            favoritesrecyclerView.adapter = adapter
        }
    }
}