package com.boubyan.articles.domain.entities

data class MediaMetadata(
    val url: String,
    val format: String,
    val height: Int,
    val width: Int
) {
    companion object {
        fun empty() = MediaMetadata(
            url = "",
            format = "",
            height = 0,
            width = 0
        )
    }
    
    val isValidSize: Boolean
        get() = height > 0 && width > 0
    
    val aspectRatio: Float
        get() = if (height > 0) width.toFloat() / height.toFloat() else 1f
    
    val resolution: String
        get() = "${width}x${height}"
}