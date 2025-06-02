package com.boubyan.articles.data.remote.mappers

import com.boubyan.articles.data.remote.dto.MediaDto
import com.boubyan.articles.domain.entities.Media
import com.boubyan.core.base.BaseMapperImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaMapper @Inject constructor(
    private val mediaMetadataMapper: MediaMetadataMapper
) : BaseMapperImpl<MediaDto, Media>() {

    override fun mapToDomain(dto: MediaDto): Media {
        return Media(
            type = dto.type.orEmpty(),
            subtype = dto.subtype.orEmpty(),
            caption = dto.caption.orEmpty(),
            copyright = dto.copyright.orEmpty(),
            approvedForSyndication = dto.approvedForSyndication,
            mediaMetadata = mediaMetadataMapper.mapToDomainList(dto.mediaMetadata)
        )
    }

    override fun mapToDto(domain: Media): MediaDto {
        return MediaDto(
            type = domain.type,
            subtype = domain.subtype,
            caption = domain.caption,
            copyright = domain.copyright,
            approvedForSyndication = domain.approvedForSyndication,
            mediaMetadata = mediaMetadataMapper.mapToDtoList(domain.mediaMetadata)
        )
    }
}