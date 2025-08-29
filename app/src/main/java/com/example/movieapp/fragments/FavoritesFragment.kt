package com.example.movieapp.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.adapters.FavoritesAdapter
import com.example.movieapp.data.Movie
import com.example.movieapp.database.DatabaseProvider
import com.example.movieapp.databinding.FragmentFavoritesBinding
import com.example.movieapp.viewmodels.FavoritesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch



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
            MaterialAlertDialogBuilder(requireContext())
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
                    not_exist_favorites()
                } else {
                    exist_favorites(favmovies)
                }
                viewModel.markDataAsFetched()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.loading.collect { isloading ->
                if (isloading) {
                    binding.loadingAnimationView.visibility = View.VISIBLE
                    binding.favoritesrecyclerView.visibility = View.GONE
                    binding.brokenHeartAnimationView.visibility = View.GONE
                    binding.nofavoritestextView.visibility = View.GONE
                } else {
                    binding.loadingAnimationView.visibility = View.GONE
                    binding.favoritesrecyclerView.visibility = View.VISIBLE
                }

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
            adapter = FavoritesAdapter(favmovies, this@FavoritesFragment)
            favoritesrecyclerView.adapter = adapter
        }
    }
}