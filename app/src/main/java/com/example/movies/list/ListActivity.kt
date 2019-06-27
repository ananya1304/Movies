package com.example.movies.list

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.movies.R
import com.example.movies.utils.NotificationReciever
import com.example.movies.utils.replaceFragmentInActivity
import java.util.*

class ListActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        supportActionBar!!.title = getString(R.string.most_popular_movies)
        val listFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        as ListFragment? ?: ListFragment.newInstance(this).also{
            replaceFragmentInActivity(it, R.id.frameLayout)
        }
    }

    companion object {

        fun newInstance() = ListActivity()
    }
}
