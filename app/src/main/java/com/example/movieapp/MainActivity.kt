package com.example.movieapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {   //add fragment to activity
            add(R.id.frame_content, MoviesFragment())
        }

        val animationCovers = listOf(
            MovieCover(R.drawable.cars),
            MovieCover(R.drawable.despme4),
            MovieCover(R.drawable.insideout2),
            MovieCover(R.drawable.toystory4)

        )

        val actionCovers = listOf(
            MovieCover(R.drawable.sanandreas),
            MovieCover(R.drawable.missionimpossible),
            MovieCover(R.drawable.thefallguy),
            MovieCover(R.drawable.topgunmaverick),
            MovieCover(R.drawable.fastx)
        )

        val dramaCategory = listOf(
            MovieCover(R.drawable.shidlerlist),
            MovieCover(R.drawable.shawshankredemption),
            MovieCover(R.drawable.lion),
            MovieCover(R.drawable.godfather3),
            MovieCover(R.drawable.theblindside)
        )



        val actionCategory = listOf(
            MovieCategories("Action",actionCovers),
            MovieCategories("Animated",animationCovers),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory),
            MovieCategories("Drama",dramaCategory)
        )

        val catrecyclerView = findViewById<RecyclerView>(R.id.catrecyclerView)
        catrecyclerView.adapter = MainAdapter(actionCategory)
    }


}