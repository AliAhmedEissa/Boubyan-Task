package com.boubyan.articles.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MediaMetadataDto(
    @SerializedName("url")
    val url: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int
)