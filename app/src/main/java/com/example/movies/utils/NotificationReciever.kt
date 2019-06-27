package com.example.movies.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.example.movies.list.ListActivity

class NotificationReciever: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var notificationManager: NotificationManager = p0!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val repeatingIntent: Intent = Intent(p0, ListActivity::class.java)
        repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        var pendingIntent: PendingIntent = PendingIntent.getActivity(p0, 100, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT )

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(p0)
            .setContentIntent(pendingIntent)
            .setSmallIcon(android.R.drawable.arrow_up_float)
            .setContentTitle("Are you bored?")
            .setContentText("Browse latest movies now!")
            .setAutoCancel(true)

        notificationManager.notify(100,builder.build())
    }

}