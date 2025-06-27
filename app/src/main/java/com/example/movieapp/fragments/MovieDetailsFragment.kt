package com.example.movieapp.fragments

import android.content.Intent

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.network.RetroifitInstance
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {


    private lateinit var binding: FragmentMovieDetailsBinding
    var heart_state = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getsetDatatoFragment()
        favbutton_handler()
    }

    fun favbutton_handler() {
        binding.favbutton.setOnClickListener {
            if (heart_state) {
                binding.favbutton.setImageResource(R.drawable.favoritefilled_24)
                heart_state = false
            } else {
                binding.favbutton.setImageResource(R.drawable.favorite_24)
                heart_state = true
            }
        }
    }


    private fun getsetDatatoFragment() {
        val movieId = arguments?.getString("idkey")?.toInt() ?: 0

        getDirector(movieId) //fix the slow getting of director
        getGenre(movieId)
        getDuration(movieId)
        getTrailer(movieId)

        binding.sharebuttonbutton.setOnClickListener {
            shareMovie(movieId)
        }

        val getTitle = arguments?.getString("titlekey")
        binding.movieTitleDe.text = "$getTitle"

        val getOverview = arguments?.getString("overviewkey")
        binding.movieOverviewDe.text = "$getOverview"

        val getReleaseDate = arguments?.getString("releasekey")
        binding.YearField.text = "$getReleaseDate"

        val getCover = arguments?.getString("coverkey") //MovieCover
        Glide.with(requireContext())
            .load(getCover)
            .into(binding.movieCoverDe)
    }

    private fun shareMovie(movieId: Int) {
        val movieURL = "https://www.themoviedb.org/movie/$movieId"

        val share = Intent().apply { // use apply to set the intent properties
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, movieURL)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(share, movieURL))
    }

    private fun getTrailer(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetroifitInstance.api.getMovieVideos(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val trailer = response.results.find { it.type == "Trailer" && it.site == "YouTube" }
                if (trailer != null) {
                    val trailerUrl = "https://www.youtube.com/watch?v=${trailer.key}"
                    binding.trailerbt.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                        startActivity(intent)
                    }
                } else {
                    binding.trailerbt.setOnClickListener {
                        Snackbar.make(binding.root, "No trailer found", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getDirector(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetroifitInstance.api.getMovieCredits(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val director = response.crew.find { it.job == "Director" }?.name ?: "Unknown"
                binding.DirectorsName.text = director
            }
        }
    }

    private fun getGenre(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetroifitInstance.api.getMovieDetails(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val genre = response.genres.take(2)
                    .joinToString(", ") { it.name } //takes only 2 genres and not all
                binding.GenreField.text = genre
            }
        }
    }

    private fun getDuration(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetroifitInstance.api.getMovieDetails(
                movieId = movieId,
                apiKey = APIKEY
            )
            Log.d("SeeResponse", response.toString())
            withContext(Dispatchers.Main) {
                val duration = response.runtime
                val convert = if (duration < 60) {
                    "$duration min"
                } else {
                    val hours = duration / 60
                    val minutes = duration % 60
                    "$hours h $minutes min"
                }
                binding.DurationField.text = convert
            }
        }
    }
}