package com.example.movieapp.data


data class MovieCategories(val cat: String, val moviecoverchild: List<Movie>)

data class MovieResponse(val results: List<Movie>)

//Same JSON
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
)

//Crew Members (Directors)
data class CreditsResponse(
    val crew: List<CrewMember>
)

data class CrewMember(
    val name: String,
    val job: String
)

//Genre
data class MovieDetailsResponse(
    val genres: List<MoreDetails>,
    val runtime: Int
)

data class MoreDetails(
    val id: Int,
    val name: String,
)

//Trailer
data class VideoResponse(
    val results: List<Video>
)

data class Video(
    val key: String,
    val name: String,
    val site: String,  //"YouTube"
    val type: String   //"Trailer", "Teaser"
)

//Backdrops
data class BackdropResponse( // na ta sviso ean den ta xrisimopoiso
    val backdrops: List<Backdrop>
)

data class Backdrop(
    val file_path: String,
    val width: Int,
    val height: Int
)




