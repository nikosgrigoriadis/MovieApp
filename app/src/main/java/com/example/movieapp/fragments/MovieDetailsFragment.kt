package com.example.movieapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.RetroifitInstance
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
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
        button_handler()
    }

    fun button_handler() {
        binding.favbutton.setOnClickListener {
            if (heart_state) {
                binding.favbutton.setImageResource(R.drawable.favoritefilled_24)
                heart_state = false
            }
            else {
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
                val genre = response.genres.joinToString(", ") { it.name }
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

