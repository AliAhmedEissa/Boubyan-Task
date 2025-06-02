package com.boubyan.articles.data.remote.mappers

import com.boubyan.articles.data.remote.dto.ArticleDto
import com.boubyan.articles.domain.entities.Article
import com.boubyan.core.base.BaseMapperImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleMapper @Inject constructor(
    private val mediaMapper: MediaMapper
) : BaseMapperImpl<ArticleDto, Article>() {

    override fun mapToDomain(dto: ArticleDto): Article {
        return Article(
            id = dto.id,
            url = dto.url.orEmpty(),
            adxKeywords = dto.adxKeywords,
            column = dto.column,
            section = dto.section.orEmpty(),
            byline = dto.byline.orEmpty(),
            type = dto.type.orEmpty(),
            title = dto.title.orEmpty(),
            abstract = dto.abstract.orEmpty(),
            publishedDate = dto.publishedDate.orEmpty(),
            source = dto.source.orEmpty(),
            views = dto.views.coerceAtLeast(0),
            media = mediaMapper.mapToDomainList(dto.media),
            etaId = dto.etaId
        )
    }

    override fun mapToDto(domain: Article): ArticleDto {
        return ArticleDto(
            id = domain.id,
            url = domain.url,
            adxKeywords = domain.adxKeywords,
            column = domain.column,
            section = domain.section,
            byline = domain.byline,
            type = domain.type,
            title = domain.title,
            abstract = domain.abstract,
            publishedDate = domain.publishedDate,
            source = domain.source,
            views = domain.views,
            media = mediaMapper.mapToDtoList(domain.media),
            etaId = domain.etaId
        )
    }
}