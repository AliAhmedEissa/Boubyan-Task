package com.boubyan.articles.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("url")
    val url: String,
    @SerializedName("adx_keywords")
    val adxKeywords: String?,
    @SerializedName("column")
    val column: String?,
    @SerializedName("section")
    val section: String,
    @SerializedName("byline")
    val byline: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("abstract")
    val abstract: String,
    @SerializedName("published_date")
    val publishedDate: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("views")
    val views: Long,
    @SerializedName("media")
    val media: List<MediaDto>,
    @SerializedName("eta_id")
    val etaId: Long
)