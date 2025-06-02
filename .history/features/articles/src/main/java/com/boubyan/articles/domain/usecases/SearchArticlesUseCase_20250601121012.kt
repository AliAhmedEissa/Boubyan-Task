package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<SearchArticlesUseCase.Params, List<Article>>() {

    data class Params(
        val query: String, 
        val period: Period = Period.getDefault(), 
        val minQueryLength: Int = 2
    )

    override fun execute(parameters: Params): Flow<Resource<List<Article>>> {
        if (parameters.query.length < parameters.minQueryLength) {
            return flowOf(
                Resource.Error(IllegalArgumentException("Query must be at least ${parameters.minQueryLength} characters"))
            )
        }

        return articlesRepository.searchArticles(parameters.query.trim(), parameters.period)
    }
}