package com.example.movies.list

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.movies.R
import com.example.movies.model.Movie
import com.example.movies.network.ApiClient

class MoviesAdapter(private val listActivity: ListActivity, private var movieList: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movie = movieList[position]

        holder.tvMovieTitle.text = movie.title
        holder.tvMovieRatings.text = (movie.rating).toString()
        holder.tvReleaseDate.text = movie.releaseDate

        // loading album cover using Glide library
        Glide.with(listActivity)
            .load(ApiClient.IMAGE_BASE_URL + movie.thumbPath)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pbLoadImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pbLoadImage.visibility = View.GONE
                    return false
                }
            })
            .apply(RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
            .into(holder.ivMovieThumb)

        holder.itemView.setOnClickListener { listActivity.onMovieItemClick(position) }

    }

    override fun getItemCount(): Int {
        return movieList.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvMovieTitle: TextView

        var tvMovieRatings: TextView

        var tvReleaseDate: TextView

        var ivMovieThumb: ImageView

        var pbLoadImage: ProgressBar

        init {

            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title)
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date)
            tvMovieRatings = itemView.findViewById(R.id.tv_movie_ratings)
            ivMovieThumb = itemView.findViewById(R.id.iv_movie_thumb)
            pbLoadImage = itemView.findViewById(R.id.pb_load_image)
        }
    }
}
