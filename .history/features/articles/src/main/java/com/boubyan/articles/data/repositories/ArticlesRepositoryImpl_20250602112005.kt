package com.boubyan.articles.data.repositories

import com.boubyan.articles.data.remote.datasource.ArticlesRemoteDataSource
import com.boubyan.articles.data.remote.mappers.ArticlesResponseMapper
import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.articles.domain.utils.ArticleUtils
import com.boubyan.core.base.BaseRepositoryImpl
import com.boubyan.core.base.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the ArticlesRepository interface.
 * Handles fetching and managing article data from remote sources.
 */
@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArticlesRemoteDataSource,
    private val articlesResponseMapper: ArticlesResponseMapper,
) : ArticlesRepository, BaseRepositoryImpl() {

    private var articles = emptyList<Article>()

    /**
     * Fetches the most popular articles for a specific time period.
     * Maps the remote response to domain entities and wraps the result in a Resource.
     *
     * @param period The time period to fetch articles for
     * @return Flow of Resource containing the list of articles
     */
    override fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading)
            try {
                safeApiCallWithMapping(apiCall = { remoteDataSource.getMostPopularArticles(period.value) },
                    mapper = { articlesResponseMapper.mapToDomain(it) }).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            articles = resource.data
                            emit(resource)
                        }

                        is Resource.Error -> emit(resource)
                        is Resource.Loading -> emit(resource)
                    }
                }
            } catch (networkException: Exception) {
                val userFriendlyMessage =
                    "Network error. Please check your internet connection and try again."
                emit(Resource.Error(Exception(userFriendlyMessage)))
            }
        }
    }

    /**
     * Fetches a specific article by its ID.
     * Currently implemented to fetch from the most popular articles list.
     *
     * @param articleId The ID of the article to fetch
     * @return Flow of Resource containing the article
     */
    override fun getArticleById(articleId: Long): Flow<Resource<Article>> {
        return flow {
            val article = ArticleUtils.getArticleById(articles, articleId)
            if (article != null) {
                emit(Resource.Success(article))
            } else {
                emit(Resource.Error(Exception("Article not found. It may have been removed or is no longer available.")))
            }
        }
    }

    /**
     * Searches for articles matching the given query.
     * Currently implemented to search within the most popular articles.
     *
     * @param query The search query
     * @return Flow of Resource containing the list of matching articles
     */
    override fun searchArticles(query: String): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading)
            val searchResults = ArticleUtils.searchInArticles(articles, query)
            if (searchResults.isNotEmpty()) {
                emit(Resource.Success(searchResults))
            } else {
                emit(Resource.Error(Exception("No articles found matching your search criteria.")))
            }
        }
    }
}