package com.boubyan.articles.domain.utils

import com.boubyan.articles.domain.entities.Media

object MediaUtils {
    
    fun getBestQualityImage(media: List<Media>): String? {
        return media.filter { it.isImage }
            .flatMap { it.mediaMetadata }
            .maxByOrNull { it.width * it.height }
            ?.url
    }
    
    fun getThumbnailImage(media: List<Media>): String? {
        return media.filter { it.isImage }
            .flatMap { it.mediaMetadata }
            .minByOrNull { it.width * it.height }
            ?.url
    }
    
    fun getMediumQualityImage(media: List<Media>): String? {
        val images = media.filter { it.isImage }
            .flatMap { it.mediaMetadata }
            .sortedBy { it.width * it.height }
        
        return when {
            images.isEmpty() -> null
            images.size == 1 -> images.first().url
            images.size == 2 -> images.last().url
            else -> images[images.size / 2].url
        }
    }
    
    fun getImageBySize(media: List<Media>, preferredWidth: Int): String? {
        return media.filter { it.isImage }
            .flatMap { it.mediaMetadata }
            .minByOrNull { kotlin.math.abs(it.width - preferredWidth) }
            ?.url
    }
    
    fun hasValidMedia(media: List<Media>): Boolean {
        return media.any { it.isImage && it.hasValidMetadata }
    }
    
    fun getMediaTypes(media: List<Media>): List<String> {
        return media.map { it.type }.distinct()
    }
    
    fun getAvailableSizes(media: List<Media>): List<String> {
        return media.flatMap { it.mediaMetadata }
            .map { it.resolution }
            .distinct()
            .sorted()
    }
}