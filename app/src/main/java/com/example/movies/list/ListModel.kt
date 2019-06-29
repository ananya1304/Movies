package com.example.movies.list

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.example.movies.model.MovieListResponse
import com.example.movies.network.ApiClient
import com.example.movies.network.ApiClient.API_KEY
import com.example.movies.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context.CONNECTIVITY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.movies.db.AppDatabase
import com.example.movies.model.Movie
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ListModel(context: Context) : ListContract.Model, CoroutineScope {

    private val TAG = "MovieListModel"
    private val job = Job()
    private var moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    var mContext: Context = context


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun getMovieList(onFinishedListener: ListContract.Model.OnFinishedListener, pageNo: Int) {


        if(isNetworkConnected()) {

            val apiService = ApiClient.client!!.create(ApiInterface::class.java)

            val call = apiService.getPopularMovies(API_KEY, pageNo)
            call.enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                    val movies = response.body()!!.results
                    for(i in movies!!.indices) {
                        insertMovie(movies[i])
                    }
                    Log.d(TAG, "Number of movies received: " + movies.size)
                    onFinishedListener.onFinished(movies)
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString())
                    onFinishedListener.onFailure(t)
                }
            })
        }
        else
        {
            launch(Dispatchers.Main) {
                val movies: List<Movie> = async(Dispatchers.IO) {
                    AppDatabase.getInstance(mContext).getMovieDao().getAll()
                }.await()
                onFinishedListener.onFinished(movies)
            }

        }
    }

    fun insertMovie(movie: Movie)
    {
        launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                AppDatabase.getInstance(mContext).getMovieDao().insertAll(movie)
            }.await()
        }
    }
   private fun isNetworkConnected(): Boolean
    {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

}