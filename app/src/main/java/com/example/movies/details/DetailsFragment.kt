package com.example.movies.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.movies.R
import com.example.movies.model.Cast
import com.example.movies.model.Movie
import com.example.movies.network.ApiClient
import java.util.ArrayList

class DetailsFragment: Fragment(), DetailsContract.View {

    private lateinit var presenter: DetailsPresenter
    private lateinit var ivBackdrop: ImageView
    private lateinit var pbLoadBackdrop: ProgressBar
    private lateinit var tvMovieTitle: TextView
    private lateinit var tvMovieReleaseDate: TextView
    private lateinit var tvMovieRatings: TextView
    private lateinit var tvOverview: TextView
    private lateinit var castAdapter: CastAdapter
    private lateinit var castList: MutableList<Cast>
    private lateinit var pbLoadCast: ProgressBar
    private lateinit var tvHomepageValue: TextView
    private lateinit var tvTaglineValue: TextView
    private lateinit var tvRuntimeValue: TextView
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var root: View
    private lateinit var rvCast: RecyclerView

    private var movieName: String? =null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollapsingToolbar()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstance: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_movie_details, container, false)
        with(root) {
            ivBackdrop = findViewById(R.id.iv_backdrop)
            pbLoadBackdrop = findViewById(R.id.pb_load_backdrop)
            tvMovieTitle = findViewById(R.id.tv_movie_title)
            tvMovieReleaseDate = findViewById(R.id.tv_release_date)
            tvMovieRatings = findViewById(R.id.tv_movie_ratings)
            tvOverview = findViewById(R.id.tv_movie_overview)

            castList = ArrayList()
            rvCast = findViewById(R.id.rv_cast)
            pbLoadCast = findViewById(R.id.pb_cast_loading)

            tvHomepageValue = findViewById(R.id.tv_homepage_value)
            tvTaglineValue = findViewById(R.id.tv_tagline_value)
            tvRuntimeValue = findViewById(R.id.tv_runtime_value)
            collapsingToolbar = findViewById(R.id.collapsing_toolbar)
            appBarLayout = findViewById(R.id.appbar)
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        castAdapter = CastAdapter(context, castList)
        rvCast.adapter = castAdapter

    }
    private fun initCollapsingToolbar() {
        collapsingToolbar.title = " "

        appBarLayout.setExpanded(true)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = movieName
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }
    override fun showProgress() {
        pbLoadBackdrop.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pbLoadCast.visibility = View.GONE
    }

    override fun setDataToViews(movie: Movie) {
        if (movie != null) {

            movieName = movie.title
            tvMovieTitle.setText(movie.title)
            tvMovieReleaseDate.setText(movie.releaseDate)
            tvMovieRatings.setText((movie.rating).toString())
            tvOverview.setText(movie.overview)

            // loading album cover using Glide library
            Glide.with(this)
                .load(ApiClient.BACKDROP_BASE_URL + movie.backdropPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadBackdrop.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbLoadBackdrop.visibility = View.GONE
                        return false
                    }
                })
                .apply(RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                .into(ivBackdrop)

            castList.clear()
            castList.addAll(movie.credits!!.cast)
            castAdapter.notifyDataSetChanged()

            tvTaglineValue.text = if (movie.tagline != null) movie.tagline else "N/A"
            tvHomepageValue.text = if (movie.homepage != null) movie.homepage else "N/A"
            tvRuntimeValue.text = if (movie.runTime != null) movie.runTime else "N/A"
        }    }

    override fun onResponseFailure(throwable: Throwable) {
        Snackbar.make(root.findViewById(R.id.main_content), getString(R.string.error_data), Snackbar.LENGTH_LONG).show()
    }


    companion object {

        fun newInstance() = DetailsFragment()
    }
}