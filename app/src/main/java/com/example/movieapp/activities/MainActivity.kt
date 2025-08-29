package com.example.movieapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import com.example.movieapp.fragments.FavoritesFragment
import com.example.movieapp.fragments.MoviesFragment
import com.example.movieapp.fragments.TimeManageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.commit {   //add fragment to activity
            replace(R.id.frame_content, MoviesFragment())
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemSelectedListener(this)

    }
    //add fragments to activity

    private fun onMovieClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, MoviesFragment())
        }
    }

    private fun onTimeManageClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, TimeManageFragment())
        }
    }

    private fun onFavoritesClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, FavoritesFragment())
        }
    }

    fun hideBottomNav() {
        binding.bottomNav.visibility = View.INVISIBLE
    }

    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun changeBackground() {
        binding.main.setBackgroundColor("#00FFFFFF".toColorInt())
    }
    fun changeBackgroundtoMain() {
        binding.main.setBackgroundResource(R.drawable.gradient_background)
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