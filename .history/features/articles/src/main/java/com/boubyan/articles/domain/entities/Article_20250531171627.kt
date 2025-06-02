package com.boubyan.articles.domain.entities

data class Article(
    val id: Long,
    val url: String,
    val adxKeywords: String?,
    val column: String?,
    val section: String,
    val byline: String,
    val type: String,
    val title: String,
    val abstract: String,
    val publishedDate: String,
    val source: String,
    val views: Long,
    val media: List<Media>,
    val etaId: Long
) {
    companion object {
        fun empty() = Article(
            id = 0L,
            url = "",
            adxKeywords = null,
            column = null,
            section = "",
            byline = "",
            type = "",
            title = "",
            abstract = "",
            publishedDate = "",
            source = "",
            views = 0L,
            media = emptyList(),
            etaId = 0L
        )
    }
    
    val hasMedia: Boolean
        get() = media.isNotEmpty()
    
    val primaryImageUrl: String?
        get() = media.firstOrNull()?.mediaMetadata?.lastOrNull()?.url
    
    val formattedByline: String
        get() = byline.removePrefix("By ")
    
    val isValidArticle: Boolean
        get() = id != 0L && title.isNotBlank() && url.isNotBlank()
}