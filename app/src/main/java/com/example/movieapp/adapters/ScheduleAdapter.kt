package com.example.movieapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.data.ScheduledMovieItem
import com.example.movieapp.database.MovieSchedule
import com.example.movieapp.databinding.ScheduleItemBinding
import com.example.movieapp.fragments.ScheduleFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ScheduleAdapter(
    private val moviesTMDB: List<ScheduledMovieItem>,
    private val parentFragment: ScheduleFragment
) : RecyclerView.Adapter<ScheduleAdapter.CoversAdapterViewHolder>() {

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    inner class CoversAdapterViewHolder(val binding: ScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoversAdapterViewHolder {
        val binding =
            ScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoversAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoversAdapterViewHolder, position: Int) {

        holder.binding.apply {
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
                .into(moviecoversch)


            val convertTimestamp = formatTimestamp(moviesTMDB[position].scheduledTime)

            holder.binding.schtitle.text = moviesTMDB[position].title
            holder.binding.movieScheduledTime.text = convertTimestamp

            holder.binding.imageButtonDelete.setOnClickListener {
                val convert = MovieSchedule(
                    id = moviesTMDB[position].dbID,
                    movieId = moviesTMDB[position].movieId,
                    scheduledTime = moviesTMDB[position].scheduledTime
                )
                Log.d("convert", convert.toString())
                parentFragment.binclickedsch(convert)
            }
        }
    }

    override fun getItemCount() = moviesTMDB.size
}