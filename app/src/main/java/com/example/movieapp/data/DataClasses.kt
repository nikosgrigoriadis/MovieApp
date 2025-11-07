    package com.example.movieapp.data

    import com.google.gson.annotations.SerializedName


    data class MovieCategories(val cat: String, val movies: List<Movie>)

    data class MovieResponse(val results: List<Movie>)

    //Same JSON
    data class Movie(
        val id: Int,
        val title: String,
        val overview: String,
        val poster_path: String,
        val release_date: String,
    )

    data class ScheduledMovieItem (
        val movieId: Int,
        val poster_path: String?,
        val scheduledTime: Long,
        val dbID: Int,
        val title: String,
    )

    //Crew Members (Directors, Cast)
    data class CreditsResponse(
        val crew: List<CrewMember>,
        val cast: List<CastMember>
    )

    data class CrewMember(
        val name: String,
        val profile_path: String,
        val job: String
    )

    //Genre
    data class MovieDetailsResponse(
        val genres: List<MoreDetails>,
        val runtime: Int,
        @SerializedName("vote_average")val voteAverage: Double
    )

    data class MoreDetails(
        val id: Int,
        val name: String
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
    data class BackdropResponse(
        val backdrops: List<Backdrop>
    )

    data class Backdrop(
        val file_path: String,
        val width: Int,
        val height: Int
    )

    //Cast
    data class CastMember(
        val name: String,
        val character: String,
        val profile_path: String?
    )
