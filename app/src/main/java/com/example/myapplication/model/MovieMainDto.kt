package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class MovieMainDto (
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "items") val movies: List<movie>,
)