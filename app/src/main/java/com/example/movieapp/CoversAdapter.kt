package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

data class MovieCover(@DrawableRes val image: Int)

class CoversAdapter(private val covers: List<MovieCover>) : RecyclerView.Adapter<CoversAdapter.CoversAdapterViewHolder>() {

    //Κρατάει τις αναφορές στα TextView και ImageView για κάθε στοιχείο
    class CoversAdapterViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.moviecover)
    }

    // Δημιουργεί νέες προβολές (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoversAdapterViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.slidermovieitem,parent,false)
        return CoversAdapterViewHolder(itemView)

    }

    // Αντικαθιστά το περιεχόμενο μιας προβολής (invoked by the layout manager)
    override fun onBindViewHolder(holder: CoversAdapterViewHolder, position: Int) {
        val covpos = covers[position]
        holder.coverImageView.setImageResource(covpos.image)
    }

    // Επιστρέφει το μέγεθος του dataset (invoked by the layout manager)
    override fun getItemCount() = covers.size


}