package com.boubyan.articles.data.repositories

import android.util.Log
import com.boubyan.articles.data.remote.datasource.ArticlesRemoteDataSource
import com.boubyan.articles.data.remote.mappers.ArticlesResponseMapper
import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.articles.domain.utils.ArticleUtils
import com.boubyan.core.base.BaseRepositoryImpl
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArticlesRemoteDataSource,
    private val articlesResponseMapper: ArticlesResponseMapper,
) : BaseRepositoryImpl(), ArticlesRepository {

    private val TAG = "ArticlesRepository"

    // Cache for articles to avoid repeated API calls
    private var cachedArticles: Map<String, List<Article>> = emptyMap()
    private var lastCacheTime: Map<String, Long> = emptyMap()
    private val cacheValidityDuration = 5 * 60 * 1000L // 5 minutes

    override suspend fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>> {
        Log.d(TAG, "getMostPopularArticles called for period: ${period.value}")
        val cacheKey = "articles_${period.value}"

        // Check cache first
        if (isCacheValid(cacheKey)) {
            Log.d(TAG, "Using cached articles for period: ${period.value}")
            cachedArticles[cacheKey]?.let { articles ->
                return flowOf(Resource.Success(articles))
            }
        }

        Log.d(TAG, "Fetching fresh articles from API for period: ${period.value}")
        return safeApiCallWithMapping(
            apiCall = { 
                Log.d(TAG, "Making API call to fetch articles")
                remoteDataSource.getMostPopularArticles(period.value)
            },
            mapper = { response ->
                Log.d(TAG, "API response received, mapping to domain model")
                val articles = articlesResponseMapper.mapToDomain(response)
                Log.d(TAG, "Mapped ${articles.size} articles")
                // Update cache
                updateCache(cacheKey, articles)
                articles
            }
        )
    }

    override suspend fun getArticleById(articleId: Long): Flow<Resource<Article>> {
        // Search in all cached articles first
        cachedArticles.values.forEach { articlesList ->
            val article = articlesList.find { it.id == articleId }
            if (article != null) {
                return flowOf(Resource.Success(article))
            }
        }

        // If not found in cache, try to fetch from all periods
        return getMostPopularArticles(Period.THIRTY_DAYS).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val article = resource.data.find { it.id == articleId }
                        if (article != null) {
                            Resource.Success(article)
                        } else {
                            Resource.Error(Exception("Article with ID $articleId not found"))
                        }
                    }

                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }

    override suspend fun searchArticles(query: String, period: Period): Flow<Resource<List<Article>>> {
        return getMostPopularArticles(period).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val searchResults = ArticleUtils.searchInArticles(resource.data, query)
                        Resource.Success(searchResults)
                    }

                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }

    override suspend fun getArticlesBySection(
        section: String,
        period: Period
    ): Flow<Resource<List<Article>>> {
        return getMostPopularArticles(period).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val sectionArticles = resource.data.filter {
                            it.section.equals(section, ignoreCase = true)
                        }
                        Resource.Success(sectionArticles)
                    }

                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }

    override suspend fun getAvailableSections(): Flow<Resource<List<String>>> {
        return getMostPopularArticles(Period.SEVEN_DAYS).map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val sections = ArticleUtils.extractSections(resource.data)
                        Resource.Success(sections)
                    }

                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }

    private fun isCacheValid(cacheKey: String): Boolean {
        val lastTime = lastCacheTime[cacheKey] ?: return false
        return System.currentTimeMillis() - lastTime < cacheValidityDuration
    }

    private fun updateCache(cacheKey: String, articles: List<Article>) {
        cachedArticles = cachedArticles.toMutableMap().apply {
            put(cacheKey, articles)
        }
        lastCacheTime = lastCacheTime.toMutableMap().apply {
            put(cacheKey, System.currentTimeMillis())
        }
    }

    fun clearCache() {
        cachedArticles = emptyMap()
        lastCacheTime = emptyMap()
    }
}