package com.example.movieapp.fragments

import BackdropPagerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.activities.MainActivity
import com.example.movieapp.adapters.CastAdapter
import com.example.movieapp.database.DatabaseProvider
import com.example.movieapp.database.FavoriteMovieId
import com.example.movieapp.database.MovieSchedule
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.network.RetroifitInstance.api
import com.example.movieapp.viewmodels.FavoritesViewModel
import com.example.movieapp.viewmodels.ScheduleViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModelFav: FavoritesViewModel by activityViewModels()
    private val viewModelSchedule: ScheduleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNav() //call function from main activity
        getsetDatatoFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? MainActivity)?.showBottomNav() //call function from main activity
    }

    private fun schbutton_handler(movieId: Int) {

        binding.schbutton.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Schedule Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.addOnPositiveButtonClickListener { dateSelection ->

                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Schedule Time")
                        .build()

                timePicker.addOnPositiveButtonClickListener {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = dateSelection
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    calendar.set(Calendar.MINUTE, timePicker.minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    val scheduledTime = calendar.timeInMillis
                    val schmovie = MovieSchedule(movieId = movieId, scheduledTime = scheduledTime)
                    viewModelSchedule.addScheduled(schmovie)
                    showsnackbar("sch")

                }

                timePicker.show(parentFragmentManager, "movie_time_picker")
            }

            datePicker.show(parentFragmentManager, "movie_date_picker")
        }
    }

    private fun favbutton_handler(movieId: Int) {
        val dao = DatabaseProvider.getDatabase(requireContext()).favoriteMovieDao()

        lifecycleScope.launch {
            var isFav = withContext(Dispatchers.IO) { dao.isFavorite(movieId) }
            updateSaveButtonUI(isFav)

            binding.favbutton.setOnClickListener {
                lifecycleScope.launch {
                    viewModelFav.resetFetchFlag()
                    if (isFav) {
                        withContext(Dispatchers.IO) { dao.delete(FavoriteMovieId(movieId)) }
                        isFav = false
                    } else {
                        withContext(Dispatchers.IO) { dao.insert(FavoriteMovieId(movieId)) }
                        isFav = true
                        showsnackbar("fav")
                    }
                    updateSaveButtonUI(isFav)
                }
            }
        }
    }

    private fun updateSaveButtonUI(isFav: Boolean) {
        binding.favbuttonim.setImageResource(
            if (isFav) R.drawable.favorite_white_filled
            else R.drawable.favorite_white
        )
        binding.savetext.text = if (isFav) "Saved" else "Save"
    }


    private fun getsetDatatoFragment() {
        val movieId = arguments?.getString("idkey")?.toInt() ?: 0

        schbutton_handler(movieId)
        favbutton_handler(movieId)
        getDirector(movieId)
        getGenre(movieId)
        getDuration(movieId)
        getTrailer(movieId)
        getCast(movieId)
        fetchBackdrops(movieId)

        binding.apply {

            floatingshare.setOnClickListener {
                shareMovie(movieId)
            }
            val getOverview = arguments?.getString("overviewkey")
            movieOverviewDe.text = "$getOverview"

            val getReleaseDate =
                arguments?.getString("releasekey")?.substring(0, 4) //only 4 first digits(year)
            YearField.text = "${getReleaseDate}  •"

            val getTitle = arguments?.getString("titlekey")
            movieTitleDe.text = "$getTitle"

            val getCover = arguments?.getString("coverkey") //MovieCover
            Glide.with(requireContext())
                .load(getCover)
                .into(binding.movieCoverDe)
        }
    }

    private fun getCast(movieId: Int) {
        lifecycleScope.launch {
            try {
                val response = api.getMovieCredits(
                    movieId = movieId,
                    apiKey = APIKEY
                )
                val adapter = CastAdapter(response.cast)
                binding.castRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.castRecyclerView.adapter = adapter
            } catch (e: Exception) {
                Log.e("TMDB", "Error: ${e.message}")
            }
        }
    }

    private fun getDirector(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getMovieCredits(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val director = response.crew.find { it.job == "Director" }

                binding.directorsNameTextView.text = director?.name
                val profilePath = director?.profile_path
                val imageUrl =
                    if (profilePath != null) "https://image.tmdb.org/t/p/w185$profilePath" else null
                val requestOptions = if (profilePath == null) {
                    RequestOptions()
                        .placeholder(R.drawable.notanactor)
                        .error(R.drawable.notanactor) // Keep original scale type for actual image
                } else {
                    RequestOptions().centerCrop()
                }

                Glide.with(requireContext())
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(binding.directorsImageView)
            }
        }
    }

    private fun showsnackbar(fragment: String) {
        var text = "Added to Schedule"
        var action: Fragment = ScheduleFragment()
        var navigation: (() -> Unit)? = {
            (activity as? MainActivity)?.onTimeManageClicked()
            (activity as? MainActivity)?.navigateToSchedule()
        }

        if (fragment == "fav") {
            text = "Added to Favorites"
            action = FavoritesFragment()
            navigation = {
                (activity as? MainActivity)?.onFavoritesClicked()
                (activity as? MainActivity)?.navigateToFavorites()}
            }

        Snackbar.make(
            requireView(),
            text,
            Snackbar.LENGTH_LONG
        ).setAction("View") {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_content, action)
                addToBackStack(null)
                commit()
            }
            navigation?.invoke()
            (activity as? MainActivity)?.showBottomNav()
        }.show()
    }

    private fun fetchBackdrops(movieId: Int) {
        lifecycleScope.launch {
            try {
                val response = api.getMovieBackdrops(
                    movieId = movieId,
                    apiKey = APIKEY
                )

                val adapter = BackdropPagerAdapter(response.backdrops)
                binding.backdropViewPager.adapter = adapter
            } catch (e: Exception) {
                Log.e("TMDBack", "Error: ${e.message}")
            }
        }
    }


    private fun shareMovie(movieId: Int) {
        val movieURL = "https://www.themoviedb.org/movie/$movieId"

        val share = Intent().apply { // use apply to set the intent properties
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, movieURL)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(share, movieURL))
    }

    private fun getTrailer(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getMovieVideos(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val trailer = response.results.find { it.type == "Trailer" && it.site == "YouTube" }
                if (trailer != null) {
                    val trailerUrl = "https://www.youtube.com/watch?v=${trailer.key}"
                    binding.trailerbt.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                        startActivity(intent)
                    }
                } else {
                    binding.trailerbt.setOnClickListener {
                        Snackbar.make(binding.root, "No trailer found", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getGenre(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getMovieDetails(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val genre = response.genres.take(2)
                    .joinToString(", ") { it.name } //takes only 2 genres and not all
                binding.GenreField.text = "${genre}  •"

                binding.ratingBar.rating = (response.voteAverage / 2).toFloat()
                binding.ratingBar.numStars = 5
                binding.ratingValueText.text = "${"%.1f".format(response.voteAverage)} / 10"

            }
        }
    }

    private fun getDuration(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getMovieDetails(
                movieId = movieId,
                apiKey = APIKEY
            )
            withContext(Dispatchers.Main) {
                val duration = response.runtime
                val convert = if (duration < 60) {
                    "$duration min"
                } else {
                    val hours = duration / 60
                    val minutes = duration % 60
                    "${hours}h ${minutes}m"
                }
                binding.DurationField.text = convert
            }
        }
    }

}