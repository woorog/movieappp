package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

class SearchMovieDto (
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "items") val movies: List<movie>,
    )