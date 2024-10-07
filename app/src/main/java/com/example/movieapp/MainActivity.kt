package com.example.movieapp

import android.os.Bundle

import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

import com.example.movieapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION") //na do giati bgazei tin grammi
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.commit {   //add fragment to activity
            add(R.id.frame_content, MoviesFragment()) // add -> replace
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemSelectedListener(this)

    }

    private fun onMovieClicked() {
        supportFragmentManager.commit {   //add Movie fragment to activity
            replace(R.id.frame_content, MoviesFragment())
        }
    }

    private fun onTimeManageClicked() {
        supportFragmentManager.commit {   //add Time Manage fragment to activity
            replace(R.id.frame_content, TimeManageFragment()) // add -> replace
        }
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
            else -> {
                return false
            }
        }
    }
}
