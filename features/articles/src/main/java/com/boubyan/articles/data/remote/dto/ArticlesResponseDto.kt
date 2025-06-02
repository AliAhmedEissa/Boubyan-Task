package com.boubyan.articles.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticlesResponseDto(
    @SerializedName("status")
    val status: String,
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("num_results")
    val numResults: Int,
    @SerializedName("results")
    val results: List<ArticleDto>
)