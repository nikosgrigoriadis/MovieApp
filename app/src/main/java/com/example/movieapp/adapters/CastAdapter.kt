package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.CastMember
import com.example.movieapp.databinding.CastItemBinding

class CastAdapter(private val castList: List<CastMember>) :
    RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    inner class CastViewHolder(val binding: CastItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val actor = castList[position]
        holder.binding.actorNameTextView.text = actor.name
        holder.binding.characterTextView.text = "as ${actor.character}"

        val profileUrl = actor.profile_path?.let { //for directors images
            "https://image.tmdb.org/t/p/w185$it"
        }

        val requestOptions = if (profileUrl == null) {
            RequestOptions()
                .placeholder(R.drawable.notanactor)
                .error(R.drawable.notanactor) // Keep original scale type for actual image

        } else {
            RequestOptions().centerCrop() // Use centerCrop when loading the placeholder/error drawable
        }

        Glide.with(holder.itemView)
            .load(profileUrl)
            .apply(requestOptions)
            .into(holder.binding.actorImageView)
    }

    override fun getItemCount(): Int = castList.size
}
