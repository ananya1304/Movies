package com.example.movies

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import android.content.Intent
import android.app.PendingIntent
import com.example.movies.list.ListActivity


/**
 * Implementation of App Widget functionality.
 */
class MovieProvider : AppWidgetProvider() {

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int)
    {
        val intent = Intent(context, MovieRemoteService::class.java)
        var views = RemoteViews(context.packageName, R.layout.movie_provider)
        views.setRemoteAdapter(R.id.stack_view, intent)
        views.setEmptyView(R.id.stack_view ,R.id.empty_view)

        val mainIntent = Intent(context, ListActivity::class.java)
        var mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0)
        views.setOnClickPendingIntent(R.id.empty_view, mainPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray)
    {
        val service = MovieWidgetService.newInstance()
        service.startActionUpdateWidgets(context)
    }

    fun updatePlantWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray)
    {
        for(appWidgetId in appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    companion object{
        fun newInstance() = MovieProvider()
    }
}

