package com.example.movies

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.ComponentName



class MovieWidgetService: IntentService(MovieWidgetService::class.java.simpleName){

    private val TAG = MovieWidgetService::class.java.simpleName
    val ACTION_UPDATE_WIDGETS = "com.example.movies.action.update_widgets"

    private var provider = MovieProvider.newInstance()
    override fun onHandleIntent(p0: Intent?) {
        if(p0 != null)
        {
            val action = p0.action
            if(ACTION_UPDATE_WIDGETS == action)
            {
                handleActionUpdateWidget()
            }
        }
    }

    fun startActionUpdateWidgets(context: Context)
    {
        val intent = Intent(context, MovieWidgetService::class.java)
        intent.action = ACTION_UPDATE_WIDGETS
        context.startService(intent)
    }

    private fun handleActionUpdateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val appWidgetIds = appWidgetManager
            .getAppWidgetIds(ComponentName(this, MovieProvider::class.java))
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)

        provider.updatePlantWidgets(this, appWidgetManager, appWidgetIds)
    }

    companion object{
        fun newInstance() = MovieWidgetService()
    }
}