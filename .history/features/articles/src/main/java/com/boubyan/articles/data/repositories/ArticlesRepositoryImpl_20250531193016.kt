package com.boubyan.articles.data.repositories

import android.util.Log
import com.boubyan.articles.data.remote.datasource.ArticlesRemoteDataSource
import com.boubyan.articles.data.remote.mappers.ArticlesResponseMapper
import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.articles.domain.utils.ArticleUtils
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArticlesRemoteDataSource,
    private val articlesResponseMapper: ArticlesResponseMapper,
) : ArticlesRepository {

    private val TAG = "ArticlesRepository"

    override fun getMostPopularArticles(period: Period): Flow<Resource<List<Article>>> {
        Log.d(TAG, "🔄 getMostPopularArticles called for period: ${period.value}")

        return flow {
            emit(Resource.Loading)

            try {
                Log.d(TAG, "📡 Making API call...")
                val response = remoteDataSource.getMostPopularArticles(period.value)

                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        try {
                            Log.d(TAG, "✅ API response received, mapping to domain model")
                            val articles = articlesResponseMapper.mapToDomain(responseBody)
                            Log.d(TAG, "✅ Mapped ${articles.size} articles")
                            emit(Resource.Success(articles))
                        } catch (mappingException: Exception) {
                            Log.e(TAG, "❌ Error mapping response", mappingException)
                            emit(Resource.Error(Exception("Failed to process articles: ${mappingException.message}")))
                        }
                    } ?: run {
                        Log.e(TAG, "❌ Response body is null")
                        emit(Resource.Error(Exception("No data received from server")))
                    }
                } else {
                    // Handle different HTTP error codes with user-friendly messages
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid request. Please check your connection and try again."
                        401 -> "Authentication failed. Please check app configuration."
                        403 -> "Access denied. Please check your permissions."
                        404 -> "Service not found. Please try again later."
                        429 -> "Too many requests. Please wait a moment and try again."
                        in 500..599 -> "Server error. Please try again later."
                        else -> "Something went wrong. Please try again."
                    }

                    Log.e(TAG, "❌ API Error: ${response.code()} - ${response.message()}")
                    emit(Resource.Error(Exception(errorMessage)))
                }
            } catch (networkException: Exception) {
                Log.e(TAG, "❌ Network exception occurred", networkException)
                val userFriendlyMessage =
                    "Network error. Please check your internet connection and try again."
                emit(Resource.Error(Exception(userFriendlyMessage)))
            }
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