package com.example.movieapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.commit
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.fragments.FavoritesFragment
import com.example.movieapp.fragments.MoviesFragment
import com.example.movieapp.fragments.ScheduleFragment
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        //disappear the navigation bar/line
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.commit {   //add fragment to activity
            replace(R.id.frame_content, MoviesFragment())
        }

    }
    //add fragments to activity

    private fun onMovieClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, MoviesFragment())
        }
    }

     fun onTimeManageClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, ScheduleFragment())
        }
    }

     fun onFavoritesClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, FavoritesFragment())
        }
    }

    fun hideBottomNav() {
        val params = binding.bottomNav.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as HideBottomViewOnScrollBehavior
        behavior.slideDown(binding.bottomNav)
        binding.bottomNav.visibility = View.GONE
    }

    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
        val params = binding.bottomNav.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as HideBottomViewOnScrollBehavior
        behavior.slideUp(binding.bottomNav) //show bottom_nav when is hidden
    }

    fun navigateToSchedule() {
        binding.bottomNav.selectedItemId = R.id.nav_time
    }

    fun navigateToFavorites() {
        binding.bottomNav.selectedItemId = R.id.nav_favorites
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_movies -> {
                onMovieClicked()
                return true
            }

            R.id.nav_time -> {
                onTimeManageClicked()
                return true
            }

            R.id.nav_favorites -> {
                onFavoritesClicked()
                return true
            }

            else -> {
                return false
            }
        }
    }
}