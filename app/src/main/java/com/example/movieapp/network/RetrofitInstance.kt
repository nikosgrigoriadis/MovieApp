package com.example.movieapp.network

import com.example.movieapp.data.BackdropResponse
import com.example.movieapp.data.CreditsResponse
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieDetailsResponse
import com.example.movieapp.data.MovieResponse
import com.example.movieapp.data.VideoResponse
import com.example.movieapp.data.WatchProviderResponse
import com.example.movieapp.fragments.APIKEY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {
    //Now playing
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MovieResponse

    //Upcoming
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MovieResponse

    //Directors
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): CreditsResponse

    //Genre
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MovieDetailsResponse

    // Discover movies by genre
    @GET("discover/movie")
    suspend fun getMoviesByGenreRetrofit(
        @Query("with_genres") genreId: String,
        @Query("api_key") apiKey: String = APIKEY,
        @Query("sort_by") sortBy: String? = "revenue.desc",
        @Query("page") page: Int = (1..30).random(),
        @Query("language") language: String
    ): MovieResponse

    //Top Rated
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): MovieResponse


    //Popular
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page : Int = (1..30).random(),
        @Query("language") language: String
    ): MovieResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): VideoResponse

    //Search
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String
    ): MovieResponse

     //Backdrops
        @GET("movie/{movie_id}/images")
        suspend fun getMovieBackdrops(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String
        ): BackdropResponse

    // fetch favorites
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Movie

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getWatchProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): WatchProviderResponse

}

//set the retrofit
object RetroifitInstance {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    val api: TMDBApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApi::class.java)
    }

}
