package com.example.movies.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.movies.R
import com.example.movies.utils.replaceFragmentInActivity

class ListActivity: AppCompatActivity(){

    private lateinit var listPresenter: ListPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        supportActionBar!!.setTitle(getString(R.string.most_popular_movies))

        val listFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        as ListFragment? ?: ListFragment.newInstance().also{
            replaceFragmentInActivity(it, R.id.frameLayout)
        }

        listPresenter = ListPresenter(listFragment)
    }

    companion object {

        fun newInstance() = ListActivity()
    }
}
