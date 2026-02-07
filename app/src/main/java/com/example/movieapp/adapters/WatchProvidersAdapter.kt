package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.data.Provider
import com.example.movieapp.databinding.WatchProviderItemBinding

class WatchProvidersAdapter(
    private val providers: List<Provider>
) : RecyclerView.Adapter<WatchProvidersAdapter.WatchProviderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchProviderViewHolder {
        val binding = WatchProviderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchProviderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchProviderViewHolder, position: Int) {
        holder.bind(providers[position])
    }

    override fun getItemCount() = providers.size

    inner class WatchProviderViewHolder(private val binding: WatchProviderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(provider: Provider) {
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w92${provider.logoPath}")
                .into(binding.providerLogo)

            itemView.setOnClickListener(null)
        }
    }
}
