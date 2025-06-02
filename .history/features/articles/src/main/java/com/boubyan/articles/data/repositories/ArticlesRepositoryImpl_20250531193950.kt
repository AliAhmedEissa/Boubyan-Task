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

    override suspend fun getArticleById(articleId: Long): Flow<Resource<Article>> {
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
}
}