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
        var calendar: Calendar =  Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 10)
        val intent: Intent = Intent(applicationContext, NotificationReciever::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(applicationContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        val listFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        as ListFragment? ?: ListFragment.newInstance(this).also{
            replaceFragmentInActivity(it, R.id.frameLayout)
        }
    }

    companion object {

        fun newInstance() = ListActivity()
    }
}
