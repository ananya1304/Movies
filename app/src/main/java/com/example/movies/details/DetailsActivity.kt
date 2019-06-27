package com.example.movies.details

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.movies.R
import com.example.movies.utils.Constants.KEY_MOVIE_ID
import com.example.movies.utils.replaceFragmentInActivity

class DetailsActivity: AppCompatActivity() {
    private lateinit var detailsPresenter: DetailsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = getString(R.string.most_popular_movies)


        val mIntent = intent
        val movieId: Int = mIntent.getIntExtra(KEY_MOVIE_ID, 0)
        val detailsFragment = supportFragmentManager.findFragmentById(R.id.movie_details_container)
                as DetailsFragment? ?: DetailsFragment.newInstance().also{
            replaceFragmentInActivity(it, R.id.movie_details_container)
        }

        detailsPresenter = DetailsPresenter(detailsFragment, movieId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        fun newInstance() = DetailsActivity()
    }
}