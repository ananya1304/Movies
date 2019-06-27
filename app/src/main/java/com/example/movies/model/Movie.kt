package com.example.movies.model

import android.arch.persistence.room.*
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
@Entity
data class Movie(
    @PrimaryKey @NonNull
    var id: Int ,
    @field:SerializedName("title")
    var title: String?,
    @field:SerializedName("release_date")
    var releaseDate: String?,
    @field:SerializedName("vote_average")
    var rating: Float,
    @field:SerializedName("poster_path")
    var thumbPath: String?,
    @field:SerializedName("overview")
    var overview: String?,
    @field:SerializedName("backdrop_path")
    var backdropPath: String?,
    @field:SerializedName("credits")
    var runTime: String?,
    @field:SerializedName("tagline")
    var tagline: String?,
    @field:SerializedName("homepage")
    var homepage: String?
) {

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\''.toString() +
                ", releaseDate='" + releaseDate + '\''.toString() +
                ", rating=" + rating +
                ", thumbPath='" + thumbPath + '\''.toString() +
                ", overview='" + overview + '\''.toString() +
                ", backdropPath='" + backdropPath + '\''.toString() +
                ", runTime='" + runTime + '\''.toString() +
                ", tagline='" + tagline + '\''.toString() +
                ", homepage='" + homepage + '\''.toString() +
                '}'.toString()
    }
}