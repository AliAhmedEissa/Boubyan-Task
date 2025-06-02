package com.boubyan.articles.domain.entities

data class Media(
    val type: String,
    val subtype: String,
    val caption: String,
    val copyright: String,
    val approvedForSyndication: Int,
    val mediaMetadata: List<MediaMetadata>
) {
    companion object {
        fun empty() = Media(
            type = "",
            subtype = "",
            caption = "",
            copyright = "",
            approvedForSyndication = 0,
            mediaMetadata = emptyList()
        )
    }
    
    val isImage: Boolean
        get() = type.equals("image", ignoreCase = true)
    
    val hasValidMetadata: Boolean
        get() = mediaMetadata.isNotEmpty()
    
    val thumbnailUrl: String?
        get() = mediaMetadata.firstOrNull()?.url
    
    val largeImageUrl: String?
        get() = mediaMetadata.lastOrNull()?.url
}