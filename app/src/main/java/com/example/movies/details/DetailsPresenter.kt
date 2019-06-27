package com.example.movies.details

import com.example.movies.model.Movie

class DetailsPresenter(detailsView: DetailsContract.View, id: Int, detailsModel: DetailsModel): DetailsContract.Presenter, DetailsContract.Model.OnFinishedListener {

    private var view: DetailsContract.View = detailsView
    private var movieDetailsModel: DetailsContract.Model = detailsModel
    private var movieId: Int = id


    override fun requestMovieData() {

            view.showProgress()

        movieDetailsModel.getMovieDetails(this, movieId)

    }


    override fun onFinished(movie: Movie) {


            view.hideProgress()

        view.setDataToViews(movie)
    }

    override fun onFailure(t: Throwable) {
        view.hideProgress()

        view.onResponseFailure(t)
    }

}