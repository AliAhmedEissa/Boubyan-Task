package com.boubyan.articles.domain.repositories

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {

    /**
     * Get most popular articles for a specific period
     * @param period The time period (1, 7, or 30 days)
     * @return Flow of Resource containing list of articles
     */
    fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>>

    /**
     * Get article details by ID
     * @param articleId The unique identifier of the article
     * @return Flow of Resource containing the article details
     */
    fun getArticleById(articleId: Long): Flow<Resource<Article>>

    /**
     * Search articles by query
     * @param query The search query
     * @param period The time period to search within
     * @return Flow of Resource containing list of matching articles
     */
    fun searchArticles(query: String): Flow<Resource<List<Article>>>

}