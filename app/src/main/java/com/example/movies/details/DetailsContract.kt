package com.example.movies.details


import com.example.movies.model.Movie

interface DetailsContract {
    interface Model {

        interface OnFinishedListener {
            fun onFinished(movie: Movie)

            fun onFailure(t: Throwable)
        }

        fun getMovieDetails(onFinishedListener: OnFinishedListener, movieId: Int)
    }

    interface View {

        fun showProgress()

        fun hideProgress()

        fun setDataToViews(movie: Movie)

        fun onResponseFailure(throwable: Throwable)
    }

    interface Presenter {

        fun requestMovieData()

    }
}