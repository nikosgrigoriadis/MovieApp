package com.example.movieapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.movieapp.R
import com.example.movieapp.adapters.ScheduleAdapter
import com.example.movieapp.data.ScheduledMovieItem
import com.example.movieapp.database.MovieSchedule
import com.example.movieapp.databinding.FragmentTimemanageBinding
import com.example.movieapp.viewmodels.ScheduleViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScheduleFragment : Fragment(R.layout.fragment_timemanage) {

   private lateinit var binding: FragmentTimemanageBinding

   private val viewModelSchedule: ScheduleViewModel by activityViewModels()
   private lateinit var adapter: ScheduleAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimemanageBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchscheduledviewmodel()
    }

     fun binclickedsch(movie: MovieSchedule) {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("This action will remove all your favorites. Are you sure?")
                .setPositiveButton("Yes") { dialog, _ ->
                    lifecycleScope.launch {
                        viewModelSchedule.removeScheduled(movie)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun fetchscheduledviewmodel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelSchedule.loadScheduled()
            viewModelSchedule.scheduledMovies.collect { schmovies ->
                if (schmovies.isEmpty()) {
                    withContext(Dispatchers.Main) { not_exist_scheduled() }
                } else {
                    withContext(Dispatchers.Main) { exist_scheduled(schmovies) }
                }
            }
        }
    }

    private fun not_exist_scheduled() {
        binding.apply {
            scheduledrecyclerView.visibility = View.GONE
            scheduletext.visibility = View.GONE
            scheduleAnimationView.visibility = View.VISIBLE
            noscheduletext.visibility = View.VISIBLE
        }
    }

    private fun exist_scheduled(schmovies: List<ScheduledMovieItem>) {
        binding.apply {
            scheduleAnimationView.visibility = View.GONE
            noscheduletext.visibility = View.GONE
            scheduledrecyclerView.visibility = View.VISIBLE
            scheduletext.visibility = View.VISIBLE
            adapter = ScheduleAdapter(schmovies, this@ScheduleFragment)
            scheduledrecyclerView.adapter = adapter
        }
    }
}