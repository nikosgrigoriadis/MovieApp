package com.example.movieapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.fragments.MovieDetailsFragment
import com.example.movieapp.fragments.MoviesFragment


class CoversAdapter(
    private val moviesTMDB: List<Movie>,
    private val parentFragment: MoviesFragment
) : RecyclerView.Adapter<CoversAdapter.CoversAdapterViewHolder>() {

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

        val coverUrl = covTMDBpos.poster_path?.let {
            "https://image.tmdb.org/t/p/w500$it"
        }

        val requestOptions = if (coverUrl == null) {
            RequestOptions()
                .placeholder(R.drawable.nomovieicon)
                .error(R.drawable.nomovieicon)
        } else {
            RequestOptions().centerCrop()
        }

        Glide.with(holder.itemView.context)
            .load(coverUrl)
            .apply(requestOptions)
            .into(holder.coverImageView)

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("idkey", covTMDBpos.id.toString())
                putString("titlekey", covTMDBpos.title)
                putString("overviewkey", covTMDBpos.overview)
                putString("releasekey", covTMDBpos.release_date)
                putString("coverkey", coverUrl)
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
