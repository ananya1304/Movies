package com.example.movies.list

import com.example.movies.model.Movie

class ListPresenter(listView: ListContract.View): ListContract.Presenter, ListContract.Model.OnFinishedListener{

    private val movieListModel: ListContract.Model = ListModel()


    private var view: ListContract.View? = null
    override fun setView(view: ListContract.View)
    {
        this.view = view
        requestDataFromServer()
    }

    override fun onDestroy() {
        this.view = null
    }

    override fun getMoreData(pageNo: Int) {
        if(view != null)
        {
            view?.showProgress()
        }

        movieListModel.getMovieList(this, pageNo)
    }

    override fun requestDataFromServer() {
        if(view != null)
        {
            view!!.showProgress()
        }

        movieListModel.getMovieList(this, 1)    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFinished(movieArrayList: List<Movie>) {
        view!!.setDataToRecyclerView(movieArrayList)
        if(view != null)
        {
            view!!.hideProgress()
        }

    }

    override fun onFailure(t: Throwable) {
        view!!.onResponseFailure(t)
        if(view != null)
        {
            view!!.hideProgress()
        }
    }
}