package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CoversAdapter(private val moviesTMDB: List<Movie>) : RecyclerView.Adapter<CoversAdapter.CoversAdapterViewHolder>() {


    //Κρατάει τις αναφορές στα TextView και ImageView για κάθε στοιχείο
    class CoversAdapterViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.moviecover) // maybe binding
    }

    // Δημιουργεί νέες προβολές (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoversAdapterViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.slidermovieitem,parent,false)
        return CoversAdapterViewHolder(itemView)

    }

    // Αντικαθιστά το περιεχόμενο μιας προβολής (invoked by the layout manager)
    override fun onBindViewHolder(holder: CoversAdapterViewHolder, position: Int) {
        val covTMDBpos = moviesTMDB[position]
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500${covTMDBpos.poster_path}")
            .into(holder.coverImageView)

        //click handle
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Title: ${covTMDBpos.title}", Toast.LENGTH_SHORT).show()
        }
    }

    // Επιστρέφει το μέγεθος του dataset (invoked by the layout manager)
    override fun getItemCount() = moviesTMDB.size //covers.size


}