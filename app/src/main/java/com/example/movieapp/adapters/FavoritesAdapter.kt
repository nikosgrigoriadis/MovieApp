package com.example.movieapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.fragments.FavoritesFragment
import com.example.movieapp.fragments.MovieDetailsFragment


class FavoritesAdapter(
    private val moviesTMDB: List<Movie>,
    private val parentFragment: FavoritesFragment
) : RecyclerView.Adapter<FavoritesAdapter.CoversAdapterViewHolder>() {

    class CoversAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.moviecover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoversAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.slidermovieitem, parent, false)
        return CoversAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoversAdapterViewHolder, position: Int) {
        val covTMDBpos = moviesTMDB[position]

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${covTMDBpos.poster_path}")
            .into(holder.coverImageView)


        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("idkey", covTMDBpos.id.toString())
                putString("titlekey", covTMDBpos.title)
                putString("overviewkey", covTMDBpos.overview)
                putString("releasekey", covTMDBpos.release_date)
                putString("coverkey", "https://image.tmdb.org/t/p/w500${covTMDBpos.poster_path}")
            }

            val openFragment = MovieDetailsFragment().apply {
                arguments = bundle
            }

            parentFragment.parentFragmentManager.beginTransaction()
                .replace(R.id.frame_content, openFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount() = moviesTMDB.size
}
