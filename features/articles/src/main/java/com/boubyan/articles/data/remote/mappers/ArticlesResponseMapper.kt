package com.boubyan.articles.data.remote.mappers

import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import com.boubyan.articles.domain.entities.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesResponseMapper @Inject constructor(
    private val articleMapper: ArticleMapper
) {
    fun mapToDomain(dto: ArticlesResponseDto): List<Article> {
        return try {

            if (dto.status != "OK") {
                return emptyList()
            }

            val results = dto.results
            if (results.isEmpty()) {
                return emptyList()
            }

            val articles = results.mapNotNull { articleDto ->
                try {
                    articleMapper.mapToDomain(articleDto)
                } catch (e: Exception) {
                    null // Skip invalid articles instead of crashing
                }
            }

            articles

        } catch (e: Exception) {
            // Return empty list instead of throwing exception
            emptyList()
        }
    }

    fun getResponseStatus(dto: ArticlesResponseDto): String {
        return dto.status
    }

    fun getNumResults(dto: ArticlesResponseDto): Int {
        return dto.numResults
    }
}