package com.example.movies.details

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.example.movies.db.AppDatabase
import com.example.movies.model.Movie
import com.example.movies.network.ApiClient
import com.example.movies.network.ApiClient.API_KEY
import com.example.movies.network.ApiInterface
import com.example.movies.utils.Constants.CREDITS
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class DetailsModel (context: Context): DetailsContract.Model, CoroutineScope {
    private val TAG = "DetailsModel"
    private var moviesLiveData: MutableLiveData<Movie> = MutableLiveData()
    private val job = Job()


    private var mContext: Context  = context


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun getMovieDetails(onFinishedListener: DetailsContract.Model.OnFinishedListener, movieId: Int) {

        if(isNetworkConnected()) {
            val apiService = ApiClient.client!!.create(ApiInterface::class.java)

            val call = apiService.getMovieDetails(movieId, API_KEY)
            call.enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    val movie = response.body()
                    Log.d(TAG, "Movie data received: " + movie.toString())
                    onFinishedListener.onFinished(movie!!)
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.e(TAG, t.toString())
                    onFinishedListener.onFailure(t)
                }
            })
        }
        else
        {
            getAllMovies(movieId)
            onFinishedListener.onFinished(moviesLiveData.value!!)
        }

    }

    fun getAllMovies(movieId: Int) {
        launch(Dispatchers.Main) {
            val movie: Movie = async(Dispatchers.IO) {
                AppDatabase.getInstance(mContext).getMovieDao().getById(movieId)
            }.await()
            moviesLiveData.value = movie
        }
    }

    private fun isNetworkConnected(): Boolean
    {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

}