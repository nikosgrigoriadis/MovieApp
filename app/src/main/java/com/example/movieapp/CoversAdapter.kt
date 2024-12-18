package com.example.movieapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CoversAdapter(private val moviesTMDB: List<Movie>) :
    RecyclerView.Adapter<CoversAdapter.CoversAdapterViewHolder>() {

    private val openFragment = MovieDetailsFragment()
    private val bundle = Bundle()

    //Κρατάει τις αναφορές στα TextView και ImageView για κάθε στοιχείο
    class CoversAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.moviecover) // maybe binding
    }

    // Δημιουργεί νέες προβολές (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoversAdapterViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.slidermovieitem, parent, false)
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

            bundle.putString("titlekey", covTMDBpos.title)
            openFragment.arguments = bundle

            val activityFrag = holder.itemView.context as AppCompatActivity
            activityFrag.supportFragmentManager.beginTransaction()  //.commit -> .beginTransaction()
                .replace(R.id.frame_content, openFragment)
                .addToBackStack(null)
                .commit()

        }
    }


    // Επιστρέφει το μέγεθος του dataset (invoked by the layout manager)
    override fun getItemCount() = moviesTMDB.size //covers.size


}