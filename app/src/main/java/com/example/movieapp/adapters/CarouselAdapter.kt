package com.example.movieapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.Movie
import com.example.movieapp.fragments.MovieDetailsFragment

class CarouselAdapter(private val images: List<Movie>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    private val openFragment = MovieDetailsFragment()
    private val bundle = Bundle()

    class CarouselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carouselimageView: ImageView = view.findViewById(R.id.carousel_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val covTMDBpos = images[position]
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${covTMDBpos.poster_path}")
            .into(holder.carouselimageView)

        //click handle
        holder.itemView.setOnClickListener {

            bundle.putString("idkey", "${covTMDBpos.id}")
            bundle.putString("titlekey", covTMDBpos.title)
            bundle.putString("coverkey", "https://image.tmdb.org/t/p/w500${covTMDBpos.poster_path}")
            openFragment.arguments = bundle

            val activityFrag = holder.itemView.context as AppCompatActivity
            activityFrag.supportFragmentManager.beginTransaction()  //.commit -> .beginTransaction()
                .replace(R.id.frame_content, openFragment)
                .addToBackStack(null)
                .commit()

        }

    }

    override fun getItemCount(): Int = images.size
}
