package com.example.movieapp


data class MovieCategories(val cat: String, val moviecoverchild: List<Movie>)

data class MovieResponse(val results: List<Movie>)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
)
