package com.boubyan.articles.data.remote.api

import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NYTimesApi {
    
    @GET("{period}.json")
    suspend fun getMostPopularArticles(
        @Path("period") period: String
    ): Response<ArticlesResponseDto>

}