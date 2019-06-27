package com.example.movies.list

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.example.movies.R
import com.example.movies.details.DetailsActivity
import com.example.movies.model.Movie
import com.example.movies.utils.Constants
import com.example.movies.utils.GridSpacingItemDecoration
import com.example.movies.utils.NotificationReciever
import java.util.*

class ListActivity: AppCompatActivity(), ListContract.View{

    private lateinit var presenter: ListPresenter
    private val TAG = "MovieListActivity"
    private lateinit var rvMovieList: RecyclerView
    private var moviesList: MutableList<Movie> = arrayListOf()
    private var moviesAdapter: MoviesAdapter = MoviesAdapter(this, moviesList)
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvEmptyView: TextView
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0
    private var mLayoutManager: GridLayoutManager? = null
    private lateinit var root: View

    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_movie_list)


        supportActionBar!!.title = getString(R.string.most_popular_movies)
        var calendar: Calendar =  Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 10)
        val intent: Intent = Intent(applicationContext, NotificationReciever::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        rvMovieList = findViewById(R.id.rv_movie_list)
        pbLoading = findViewById(R.id.pb_loading)
        tvEmptyView = findViewById(R.id.tv_empty_view)
        mLayoutManager = GridLayoutManager(this, 2)
        rvMovieList.adapter = moviesAdapter
        rvMovieList.layoutManager = mLayoutManager
        rvMovieList.addItemDecoration(GridSpacingItemDecoration(2, GridSpacingItemDecoration.dpToPx(this, 10), true))
        rvMovieList.itemAnimator = DefaultItemAnimator()
        setListeners()
        presenter = ListPresenter(this)
        presenter.requestDataFromServer()
    }

    private fun setListeners() {
        rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = rvMovieList.childCount
                totalItemCount = mLayoutManager!!.itemCount
                firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    presenter.getMoreData(pageNo)
                    loading = true
                }
            }
        })
    }
    fun onMovieItemClick(position: Int){
        if (position == -1) {
            return
        }
        val detailIntent: Intent = Intent(this, DetailsActivity::class.java)
        detailIntent.putExtra(Constants.KEY_MOVIE_ID, moviesList[position].id)
        startActivity(detailIntent)
    }
    override fun showProgress() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbLoading.visibility = View.GONE
    }

    override fun setDataToRecyclerView(movieArrayList: List<Movie>) {
        moviesList.addAll(movieArrayList)
        moviesAdapter.notifyDataSetChanged()
        pageNo++
    }

    override fun onResponseFailure(throwable: Throwable) {
        Log.e(TAG, throwable.message)
    }
    companion object {

        fun newInstance() = ListActivity()
    }
}
