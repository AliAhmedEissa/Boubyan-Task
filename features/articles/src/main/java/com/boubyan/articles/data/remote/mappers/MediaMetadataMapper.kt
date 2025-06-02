package com.boubyan.articles.data.remote.mappers

import com.boubyan.articles.data.remote.dto.MediaMetadataDto
import com.boubyan.articles.domain.entities.MediaMetadata
import com.boubyan.core.base.BaseMapperImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaMetadataMapper @Inject constructor() : BaseMapperImpl<MediaMetadataDto, MediaMetadata>() {
    
    override fun mapToDomain(dto: MediaMetadataDto): MediaMetadata {
        return MediaMetadata(
            url = dto.url.orEmpty(),
            format = dto.format.orEmpty(),
            height = dto.height.coerceAtLeast(0),
            width = dto.width.coerceAtLeast(0)
        )
    }
    
    override fun mapToDto(domain: MediaMetadata): MediaMetadataDto {
        return MediaMetadataDto(
            url = domain.url,
            format = domain.format,
            height = domain.height,
            width = domain.width
        )
    }
}