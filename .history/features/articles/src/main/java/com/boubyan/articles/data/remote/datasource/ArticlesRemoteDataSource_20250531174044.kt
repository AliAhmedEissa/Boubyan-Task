package com.boubyan.articles.data.remote.datasource

import com.boubyan.articles.data.remote.api.NYTimesApi
import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import com.boubyan.core.error.AppException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRemoteDataSource @Inject constructor(
    private val nyTimesApi: NYTimesApi
) {
    
    suspend fun getMostPopularArticles(period: String): Response<ArticlesResponseDto> {
        return try {
            validatePeriod(period)
            nyTimesApi.getMostPopularArticles(period)
        } catch (e: Exception) {
            throw AppException.NetworkException("Failed to fetch articles: ${e.message}")
        }
    }
    
    private fun validatePeriod(period: String) {
        val validPeriods = listOf(
            NYTimesApi.PERIOD_1_DAY,
            NYTimesApi.PERIOD_7_DAYS,
            NYTimesApi.PERIOD_30_DAYS
        )
        
        if (period !in validPeriods) {
            throw AppException.ValidationException("Invalid period: $period. Must be one of: ${validPeriods.joinToString()}")
        }
    }
}