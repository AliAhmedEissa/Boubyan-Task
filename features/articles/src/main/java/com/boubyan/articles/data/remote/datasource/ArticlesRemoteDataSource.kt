package com.boubyan.articles.data.remote.datasource

import com.boubyan.articles.data.remote.api.NYTimesApi
import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRemoteDataSource @Inject constructor(
    private val nyTimesApi: NYTimesApi
) {

    suspend fun getMostPopularArticles(period: String): Response<ArticlesResponseDto> {
        return try {
            nyTimesApi.getMostPopularArticles(period)
        } catch (e: Exception) {
            throw e
        }
    }
}