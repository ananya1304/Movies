package com.example.movies.details

import android.util.Log
import com.example.movies.model.Movie
import com.example.movies.network.ApiClient
import com.example.movies.network.ApiClient.API_KEY
import com.example.movies.network.ApiInterface
import com.example.movies.utils.Constants.CREDITS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsModel : DetailsContract.Model {
    private val TAG = "DetailsModel"

    override fun getMovieDetails(onFinishedListener: DetailsContract.Model.OnFinishedListener, movieId: Int) {

        val apiService = ApiClient.client!!.create(ApiInterface::class.java)

        val call = apiService.getMovieDetails(movieId, API_KEY, CREDITS)
        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val movie = response.body()
                Log.d(TAG, "Movie data received: " + movie!!.toString())
                onFinishedListener.onFinished(movie)
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })

    }

    companion object
    {
        fun newInstance() = DetailsModel()
    }

}