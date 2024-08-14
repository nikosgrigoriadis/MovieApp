package com.example.movieapp

import androidx.annotation.DrawableRes

data class MovieCover(@DrawableRes val image: Int)
data class MovieCategories(val cat: String ,val moviecoverchild: List<MovieCover>)

