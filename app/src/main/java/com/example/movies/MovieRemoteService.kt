package com.example.movies

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.movies.list.ListContract
import com.example.movies.list.ListModel
import com.example.movies.model.Movie


class MovieRemoteService : RemoteViewsService(){
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return MovieRemoteFactory(this.applicationContext, p0)
    }
}

class MovieRemoteFactory(var context: Context, var intent: Intent): RemoteViewsService.RemoteViewsFactory, ListContract.Model.OnFinishedListener{

    private var movies: MutableList<Movie> = mutableListOf()
    private var movieList: List<Movie> = listOf()
    private var model = ListModel(context)
    private var mAppWidgetId: Int = intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID)


    override fun onCreate() {
        model.getMovieList(this, 1)
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun onDataSetChanged() {
        model.getMovieList(this, 1)
    }

    override fun hasStableIds(): Boolean {return true}

    override fun getViewAt(p0: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setTextViewText(R.id.movie_title, movies[p0].title)
        rv.setTextViewText(R.id.movie_date, "Release Date: " + movies[p0].releaseDate)

        val extras = Bundle()
        extras.putInt("com.example.movies.EXTRA_ITEM", p0)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent)
        return rv
    }

    override fun getCount(): Int {
        return movies.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
        movies.clear()
    }
    override fun onFinished(movieArrayList: List<Movie>) {

        for(movie in movieArrayList)
        {
            movies.add(movie)
        }
    }

    override fun onFailure(t: Throwable) {
        Log.d("Widget", t.message)
    }
}