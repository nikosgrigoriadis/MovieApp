package com.example.movieapp


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("movie/popular")
    suspend fun getPopularMovies (@Query("api_key") apiKey: String): MovieResponse
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies (@Query("api_key") apiKey: String): MovieResponse
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
