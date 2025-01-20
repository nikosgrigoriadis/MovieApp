package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.FragmentMovieDetailsBinding


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private lateinit var binding: FragmentMovieDetailsBinding

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
    }


    private fun getsetDatatoFragment() {
        val getTitle = arguments?.getString("titlekey")
        binding.movieTitleDe.text = "$getTitle"

        val getOverview = arguments?.getString("overviewkey")
        binding.movieOverviewDe.text = "$getOverview"

        val getCover = arguments?.getString("coverkey") //MovieCover
        Glide.with(requireContext())
            .load(getCover)
            .into(binding.movieCoverDe)
    }
}

