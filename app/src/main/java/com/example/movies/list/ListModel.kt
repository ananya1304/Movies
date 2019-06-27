package com.example.movies.list

import android.util.Log
import com.example.movies.model.MovieListResponse
import com.example.movies.network.ApiClient
import com.example.movies.network.ApiClient.API_KEY
import com.example.movies.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListModel : ListContract.Model {

    private val TAG = "MovieListModel"

    override fun getMovieList(onFinishedListener: ListContract.Model.OnFinishedListener, pageNo: Int) {

        val apiService = ApiClient.client!!.create(ApiInterface::class.java)

        val call = apiService.getPopularMovies(API_KEY, pageNo)
        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                val movies = response.body()!!.results
                Log.d(TAG, "Number of movies received: " + movies!!.size)
                onFinishedListener.onFinished(movies)
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
                onFinishedListener.onFailure(t)
            }
        })
    }

}