package com.example.movies.model

import com.google.gson.annotations.SerializedName

class Credits {

    @SerializedName("cast")
    lateinit var cast: Collection<Cast>
}