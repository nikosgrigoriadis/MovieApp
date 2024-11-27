package com.example.movieapp


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
val randomPage = (1..15).random()

interface TMDBApi {

    //Now playing
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies (
        @Query("api_key") apiKey: String,
    ): MovieResponse

    //Upcoming
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies (
        @Query("api_key") apiKey: String
    ): MovieResponse

    //Action
    @GET("discover/movie")
    suspend fun getActionMovies (
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String = "28",
        @Query("sort_by") sortBy: String = "revenue.desc", //ταξινομηση με βάση το revenue
        @Query("page") page: Int = randomPage
    ): MovieResponse

    //Romance
    @GET("discover/movie")
    suspend fun getRomanceMovies (
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String = "10749",
        @Query("sort_by") sortBy: String = "revenue.desc",
        @Query("page") page: Int = randomPage
    ): MovieResponse

    //Top Rated
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies (
        @Query("api_key") apiKey: String
    ): MovieResponse


    //Popular
    @GET("movie/popular")
    suspend fun getPopularMovies (
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 50
    ): MovieResponse
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
