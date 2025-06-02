package com.boubyan.articles.data.remote.datasource

import android.util.Log
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
    private val TAG = "ArticlesRemoteDataSource"
    
    suspend fun getMostPopularArticles(period: String): Response<ArticlesResponseDto> {
        Log.d(TAG, "getMostPopularArticles called with period: $period")
        return try {
            validatePeriod(period)
            Log.d(TAG, "Making API call to NYTimes API")
            val response = nyTimesApi.getMostPopularArticles(period)
            Log.d(TAG, "API call completed with code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e(TAG, "Error making API call", e)
            throw AppException.NetworkException("Failed to fetch articles: ${e.message}")
        }
    }
    
    private fun validatePeriod(period: String) {
        Log.d(TAG, "Validating period: $period")
        val validPeriods = listOf(
            NYTimesApi.PERIOD_1_DAY,
            NYTimesApi.PERIOD_7_DAYS,
            NYTimesApi.PERIOD_30_DAYS
        )
        
        if (period !in validPeriods) {
            Log.e(TAG, "Invalid period: $period")
            throw AppException.ValidationException("Invalid period: $period. Must be one of: ${validPeriods.joinToString()}")
        }
        Log.d(TAG, "Period validation successful")
    }
}