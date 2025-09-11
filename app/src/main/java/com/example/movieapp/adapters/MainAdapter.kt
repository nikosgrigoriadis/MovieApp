package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.MovieCategories
import com.example.movieapp.databinding.CategoryItemBinding
import com.example.movieapp.fragments.MoviesFragment
import kotlinx.coroutines.launch

class MainAdapter(
    private val categories: List<MovieCategories>,
    private val parentFragment: MoviesFragment
) : RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder>() {

    class MainAdapterViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolder {
        val binding = CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapterViewHolder, position: Int) {
        val catpos = categories[position]
        holder.binding.categorytext.text = catpos.cat

        val coversAdapter = CoversAdapter(catpos.moviecoverchild, parentFragment)
        holder.binding.covrecyclerView.adapter = coversAdapter

        holder.binding.refreshButton.setOnClickListener {
            parentFragment.lifecycleScope.launch {
                //parentFragment.refreshCategory(catpos.cat)
            }
        }
    }

    override fun getItemCount() = categories.size
}
