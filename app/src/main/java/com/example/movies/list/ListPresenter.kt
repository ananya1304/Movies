package com.example.movies.list

import com.example.movies.model.Movie

class ListPresenter(listView: ListContract.View, listModel: ListModel): ListContract.Presenter, ListContract.Model.OnFinishedListener{


    private var movieListModel: ListContract.Model = listModel

    private var view: ListContract.View = listView


    override fun getMoreData(pageNo: Int) {
        view.showProgress()
        movieListModel.getMovieList(this, pageNo)
    }

    override fun requestDataFromServer() {
        view.showProgress()

        movieListModel.getMovieList(this, 1)    }

    override fun onFinished(movieArrayList: List<Movie>) {
        view.setDataToRecyclerView(movieArrayList)
        view.hideProgress()

    }

    override fun onFailure(t: Throwable) {
        view.onResponseFailure(t)
        view.hideProgress()
    }
}