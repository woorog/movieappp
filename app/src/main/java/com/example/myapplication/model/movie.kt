package com.example.myapplication.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class movie (
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "pubDate") val pubDate: String,
    @SerializedName(value = "userRating") val userRating: String,
    @SerializedName(value = "link") val link: String,
    @SerializedName(value = "image") val image: String,

    ):Parcelable