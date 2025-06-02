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
import com.boubyan.core.error.AppException
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
        return try {
            val response = remoteDataSource.getMostPopularArticles(period.value)
            
            when {
                response.isSuccessful -> {
                    Log.d(TAG, "API call successful")
                    response.body()?.let { responseDto ->
                        val articles = articlesResponseMapper.mapToDomain(responseDto)
                        Log.d(TAG, "Mapped ${articles.size} articles")
                        // Update cache
                        updateCache(cacheKey, articles)
                        flowOf(Resource.Success(articles))
                    } ?: flowOf(Resource.Error(AppException.UnknownException("Empty response body")))
                }
                response.code() == 401 -> {
                    Log.e(TAG, "API key error")
                    flowOf(Resource.Error(AppException.UnauthorizedException("Invalid API key")))
                }
                response.code() == 403 -> {
                    Log.e(TAG, "Access forbidden")
                    flowOf(Resource.Error(AppException.ForbiddenException("Access forbidden")))
                }
                response.code() == 404 -> {
                    Log.e(TAG, "Resource not found")
                    flowOf(Resource.Error(AppException.NotFoundException("Resource not found")))
                }
                response.code() == 429 -> {
                    Log.e(TAG, "Rate limit exceeded")
                    flowOf(Resource.Error(AppException.RateLimitException("Rate limit exceeded")))
                }
                response.code() in 500..599 -> {
                    Log.e(TAG, "Server error: ${response.code()}")
                    flowOf(Resource.Error(AppException.ServerException("Server error: ${response.code()}")))
                }
                else -> {
                    Log.e(TAG, "Unknown error: ${response.code()}")
                    flowOf(Resource.Error(AppException.UnknownException("Unknown error: ${response.code()}")))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching articles", e)
            flowOf(Resource.Error(AppException.NetworkException("Failed to fetch articles: ${e.message}")))
        }
    }

    override fun getArticleById(articleId: Long): Flow<Resource<Article>> {
        Log.d(TAG, "🔍 getArticleById called for ID: $articleId")

        return getMostPopularArticles(Period.THIRTY_DAYS).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val article = resource.data.find { it.id == articleId }
                    if (article != null) {
                        Log.d(TAG, "✅ Found article: ${article.title}")
                        Resource.Success(article)
                    } else {
                        Log.e(TAG, "❌ Article with ID $articleId not found")
                        Resource.Error(Exception("Article not found. It may have been removed or is no longer available."))
                    }
                }

                is Resource.Error -> {
                    Log.e(TAG, "❌ Error fetching articles for article lookup")
                    Resource.Error(Exception("Unable to load article details. Please try again."))
                }

                is Resource.Loading -> resource
            }
        }
    }

    override fun searchArticles(query: String, period: Period): Flow<Resource<List<Article>>> {
        Log.d(TAG, "🔍 searchArticles called with query: '$query', period: ${period.value}")

        return getMostPopularArticles(period).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    try {
                        Log.d(TAG, "🔍 Searching in ${resource.data.size} articles")
                        val searchResults = ArticleUtils.searchInArticles(resource.data, query)
                        Log.d(TAG, "🔍 Found ${searchResults.size} matching articles")
                        Resource.Success(searchResults)
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error during search", e)
                        Resource.Error(Exception("Search failed. Please try again."))
                    }
                }

                is Resource.Error -> Resource.Error(Exception("Unable to search articles. Please try again."))
                is Resource.Loading -> resource
            }
        }
    }

    override fun getArticlesBySection(
        section: String,
        period: Period
    ): Flow<Resource<List<Article>>> {
        Log.d(TAG, "📂 getArticlesBySection called for section: '$section', period: ${period.value}")

        return getMostPopularArticles(period).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    try {
                        val sectionArticles = resource.data.filter {
                            it.section.equals(section, ignoreCase = true)
                        }
                        Log.d(TAG, "📂 Found ${sectionArticles.size} articles in section '$section'")
                        Resource.Success(sectionArticles)
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error filtering by section", e)
                        Resource.Error(Exception("Unable to load section articles. Please try again."))
                    }
                }

                is Resource.Error -> Resource.Error(Exception("Unable to load section articles. Please try again."))
                is Resource.Loading -> resource
            }
        }
    }

    override fun getAvailableSections(): Flow<Resource<List<String>>> {
        Log.d(TAG, "📂 getAvailableSections called")

        return getMostPopularArticles(Period.SEVEN_DAYS).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    try {
                        val sections = ArticleUtils.extractSections(resource.data)
                        Log.d(TAG, "📂 Found ${sections.size} sections: $sections")
                        Resource.Success(sections)
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error extracting sections", e)
                        Resource.Error(Exception("Unable to load sections. Please try again."))
                    }
                }

                is Resource.Error -> Resource.Error(Exception("Unable to load sections. Please try again."))
                is Resource.Loading -> resource
            }
        }
    }
}