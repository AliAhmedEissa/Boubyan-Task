package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<SearchArticlesUseCase.Params, List<Article>>() {

    data class Params(
        val query: String, val period: Period = Period.getDefault(), val minQueryLength: Int = 2
    )

    override fun execute(parameters: Params): Flow<Resource<List<Article>>> {
        if (parameters.query.length < parameters.minQueryLength) {
            return flowOf(
                Resource.Error(IllegalArgumentException("Query must be at least ${parameters.minQueryLength} characters"))
            )
        }

        return flow {
            articlesRepository.searchArticles(parameters.query.trim(), parameters.period)
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val relevantArticles =
                                rankSearchResults(resource.data, parameters.query)
                            Resource.Success(relevantArticles)
                        }

                        is Resource.Error -> resource
                        is Resource.Loading -> resource
                        else -> Resource.Success(resource.isSuccess)
                    }
                }
        }
    }

    private fun rankSearchResults(articles: List<Article>, query: String): List<Article> {
        val queryLower = query.lowercase()

        return articles.sortedByDescending { article ->
            var score = 0

            // Title match gets highest score
            if (article.title.lowercase().contains(queryLower)) {
                score += 10
            }

            // Abstract match gets medium score
            if (article.abstract.lowercase().contains(queryLower)) {
                score += 5
            }

            // Section match gets lower score
            if (article.section.lowercase().contains(queryLower)) {
                score += 2
            }

            // Keywords match gets lower score
            if (article.adxKeywords?.lowercase()?.contains(queryLower) == true) {
                score += 1
            }

            score
        }
    }
}