package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val categories: List<MovieCategories>) : RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder>() {
    class MainAdapterViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val categoryTextView: TextView = itemView.findViewById(R.id.categorytext)
        val nestedRecyclerView: RecyclerView = itemView.findViewById(R.id.covrecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return MainAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainAdapterViewHolder, position: Int) {
        val catpos = categories[position]
        holder.categoryTextView.text = catpos.cat
        val coversadapter = CoversAdapter(catpos.moviecoverchild)
        holder.nestedRecyclerView.adapter = coversadapter
    }

    override fun getItemCount() = categories.size
}