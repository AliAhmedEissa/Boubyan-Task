package com.boubyan.articles.domain.entities

/**
 * Data class representing an article from the NY Times API.
 * Contains all the necessary information about an article including its content,
 * metadata, and associated media.
 */
data class Article(
    /** Unique identifier for the article */
    val id: Long,
    /** URL where the article can be accessed */
    val url: String,
    /** Keywords for advertising purposes */
    val adxKeywords: String?,
    /** Column name if the article is part of a column */
    val column: String?,
    /** Section/category of the article */
    val section: String,
    /** Author information */
    val byline: String,
    /** Type of content */
    val type: String,
    /** Article title */
    val title: String,
    /** Article summary/abstract */
    val abstract: String,
    /** Publication date */
    val publishedDate: String,
    /** Source of the article */
    val source: String,
    /** Number of views/reads */
    val views: Long,
    /** List of media associated with the article */
    val media: List<Media>,
    /** ETA identifier */
    val etaId: Long
) {
    companion object {
        /**
         * Creates an empty Article instance with default values.
         * Used as a fallback when article data is not available.
         *
         * @return An empty Article instance
         */
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
    
    /**
     * Returns true if the article has any associated media.
     */
    val hasMedia: Boolean
        get() = media.isNotEmpty()
    
    /**
     * Returns the URL of the primary image associated with the article.
     * Returns null if no media is available.
     */
    val primaryImageUrl: String?
        get() = media.firstOrNull()?.mediaMetadata?.lastOrNull()?.url
    
    /**
     * Returns the byline without the "By " prefix.
     */
    val formattedByline: String
        get() = byline.removePrefix("By ")
    
    /**
     * Returns true if the article has valid required fields (id, title, and url).
     */
    val isValidArticle: Boolean
        get() = id != 0L && title.isNotBlank() && url.isNotBlank()
}