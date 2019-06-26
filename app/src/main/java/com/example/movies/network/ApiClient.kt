package com.example.movies.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val BASE_URL = "http://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null
    val API_KEY = "3b3328cf471da66ac6da6666249878b7"
    val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w200/"
    val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/"

    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}