package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentMoviesBinding


class MoviesFragment: Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animationCovers = listOf(
            MovieCover(R.drawable.cars),
            MovieCover(R.drawable.despme4),
            MovieCover(R.drawable.insideout2),
            MovieCover(R.drawable.toystory4)

        )

        val actionCovers = listOf(
            MovieCover(R.drawable.sanandreas),
            MovieCover(R.drawable.missionimpossible),
            MovieCover(R.drawable.thefallguy),
            MovieCover(R.drawable.topgunmaverick),
            MovieCover(R.drawable.fastx)
        )

        val dramaCategory = listOf(
            MovieCover(R.drawable.shidlerlist),
            MovieCover(R.drawable.shawshankredemption),
            MovieCover(R.drawable.lion),
            MovieCover(R.drawable.godfather3),
            MovieCover(R.drawable.theblindside)
        )

        val actionCategory = listOf(
            MovieCategories("Action",actionCovers),
            MovieCategories("Animated",animationCovers),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory)
        )

        val catrecyclerView = binding.catrecyclerView //metakinisi i oxi
        catrecyclerView.adapter = MainAdapter(actionCategory) //na lyso to crash
    }
}