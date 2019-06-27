package com.example.movies.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.movies.R
import com.example.movies.model.Movie
import com.example.movies.utils.Constants.KEY_MOVIE_ID
import com.example.movies.utils.GridSpacingItemDecoration
import com.example.movies.utils.GridSpacingItemDecoration.Companion.dpToPx
import java.util.ArrayList

class ListFragment: Fragment(), ListContract.View {
    override var presenter: ListContract.Presenter
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    private val TAG = "MovieListActivity"
    private lateinit var rvMovieList: RecyclerView
    private lateinit var moviesList: MutableList<Movie>
    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvEmptyView: TextView
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0
    private var mLayoutManager: GridLayoutManager? = null

    private var pageNo = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        presenter.setView(this)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstance: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_movie_list, container, false)
        with(root) {
            rvMovieList = findViewById(R.id.rv_movie_list)

            moviesList = ArrayList()
            moviesAdapter = MoviesAdapter(ListFragment.newInstance(), moviesList)

            mLayoutManager = GridLayoutManager(ListActivity.newInstance(), 2)
            rvMovieList.layoutManager = mLayoutManager
            rvMovieList.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(ListActivity.newInstance(), 10), true))
            rvMovieList.itemAnimator = DefaultItemAnimator()
            rvMovieList.adapter = moviesAdapter

            pbLoading = findViewById(R.id.pb_loading)

            tvEmptyView = findViewById(R.id.tv_empty_view)
        }

        return root
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

        fun newInstance() = ListFragment()
    }

    fun onMovieItemClick(position: Int){
        if (position == -1) {
            return
        }
        val detailIntent = Intent(this, DetailsActivity::class.java)
        detailIntent.putExtra(KEY_MOVIE_ID, moviesList[position].id)
        startActivity(detailIntent)
    }

    fun showEmptyView()
    {
        rvMovieList.visibility = View.GONE
        tvEmptyView.visibility = View.VISIBLE
    }

    fun hideEmptyView()
    {
        rvMovieList.visibility = View.VISIBLE
        tvEmptyView.visibility = View.GONE
    }
}