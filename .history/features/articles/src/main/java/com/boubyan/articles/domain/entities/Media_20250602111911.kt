package com.boubyan.articles.domain.entities

/**
 * Data class representing media content associated with an article.
 * Contains information about the media type, metadata, and associated images.
 */
data class Media(
    /** Type of media (e.g., "image", "video") */
    val type: String,
    /** Subtype of media (e.g., "photo", "thumbnail") */
    val subtype: String,
    /** Caption describing the media content */
    val caption: String,
    /** Copyright information for the media */
    val copyright: String,
    /** Flag indicating if the media is approved for syndication */
    val approvedForSyndication: Int,
    /** List of metadata for different sizes/versions of the media */
    val mediaMetadata: List<MediaMetadata>
) {
    companion object {
        /**
         * Creates an empty Media instance with default values.
         * Used as a fallback when media data is not available.
         *
         * @return An empty Media instance
         */
        fun empty() = Media(
            type = "",
            subtype = "",
            caption = "",
            copyright = "",
            approvedForSyndication = 0,
            mediaMetadata = emptyList()
        )
    }
    
    /**
     * Returns true if this media is an image.
     */
    val isImage: Boolean
        get() = type.equals("image", ignoreCase = true)
    
    /**
     * Returns true if this media has valid metadata.
     */
    val hasValidMetadata: Boolean
        get() = mediaMetadata.isNotEmpty()
    
    /**
     * Returns the URL of the thumbnail version of the media.
     * Returns null if no metadata is available.
     */
    val thumbnailUrl: String?
        get() = mediaMetadata.firstOrNull()?.url
    
    /**
     * Returns the URL of the large version of the media.
     * Returns null if no metadata is available.
     */
    val largeImageUrl: String?
        get() = mediaMetadata.lastOrNull()?.url
}