package com.boubyan.articles.presentation.models


data class ArticleUiModel(
    val id: Long,
    val title: String,
    val abstract: String,
    val byline: String,
    val section: String,
    val publishedDate: String,
    val views: String,
    val imageUrl: String?,
    val url: String,
    val hasMedia: Boolean,
    val formattedDate: String,
    val viewsText: String,
    val sectionTag: String
) {
    companion object {
        fun empty() = ArticleUiModel(
            id = 0,
            title = "",
            abstract = "",
            byline = "",
            section = "",
            publishedDate = "",
            views = "0",
            imageUrl = null,
            url = "",
            hasMedia = false,
            formattedDate = "",
            viewsText = "",
            sectionTag = ""
        )
    }
}