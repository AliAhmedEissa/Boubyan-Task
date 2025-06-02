package com.boubyan.articles.data.remote.api

import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesApi {
    
    @GET("svc/mostpopular/v2/viewed.json")
    suspend fun getMostPopularArticles(
        @Query("period") period: String
    ): Response<ArticlesResponseDto>
    
    companion object {
        const val BASE_URL = "https://api.nytimes.com/"
        
        // Available periods
        const val PERIOD_1_DAY = "1"
        const val PERIOD_7_DAYS = "7" 
        const val PERIOD_30_DAYS = "30"
    }
}