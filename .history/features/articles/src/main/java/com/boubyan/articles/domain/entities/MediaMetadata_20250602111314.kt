package com.boubyan.articles.domain.entities

/**
 * Data class representing metadata for media content (images, videos, etc.).
 * Contains information about the media's URL, format, dimensions, and provides
 * utility methods for media handling.
 */
data class MediaMetadata(
    /** The URL where the media content can be accessed */
    val url: String,
    /** The format of the media content (e.g., "jpg", "png") */
    val format: String,
    /** The height of the media content in pixels */
    val height: Int,
    /** The width of the media content in pixels */
    val width: Int
) {
    companion object {
        /**
         * Creates an empty MediaMetadata instance with default values.
         * Used as a fallback when media metadata is not available.
         *
         * @return An empty MediaMetadata instance
         */
        fun empty() = MediaMetadata(
            url = "",
            format = "",
            height = 0,
            width = 0
        )
    }
    
    /**
     * Returns true if the media has valid dimensions (both height and width are greater than 0).
     */
    val isValidSize: Boolean
        get() = height > 0 && width > 0
    
    /**
     * Calculates the aspect ratio of the media (width / height).
     * Returns 1.0 if height is 0 to avoid division by zero.
     */
    val aspectRatio: Float
        get() = if (height > 0) width.toFloat() / height.toFloat() else 1f
    
    /**
     * Returns a string representation of the media resolution (e.g., "1920x1080").
     */
    val resolution: String
        get() = "${width}x${height}"
}