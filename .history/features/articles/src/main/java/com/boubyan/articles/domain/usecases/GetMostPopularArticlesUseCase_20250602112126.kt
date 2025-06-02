package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.entities.ArticleFilter
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for fetching and filtering most popular articles.
 * Implements the BaseUseCase pattern for handling article retrieval and filtering operations.
 */
class GetMostPopularArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<GetMostPopularArticlesUseCase.Params, List<Article>>() {

    /**
     * Data class containing the parameters required for fetching articles.
     * @param filter The filter criteria to apply to the articles
     */
    data class Params(
        val filter: ArticleFilter = ArticleFilter.default()
    )

    /**
     * Executes the article fetching and filtering operation.
     * Fetches articles from the repository and applies the specified filters.
     *
     * @param parameters The parameters containing the filter criteria
     * @return Flow of Resource containing the filtered list of articles
     */
    override fun execute(parameters: Params): Flow<Resource<List<Article>>> {
        return articlesRepository.getMostPopularArticles(parameters.filter.period)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val filteredArticles = applyFilters(resource.data, parameters.filter)
                        Resource.Success(filteredArticles)
                    }
                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
            }
    }

    /**
     * Applies filtering and sorting to the list of articles based on the provided filter criteria.
     * Filters by section and media presence, then sorts according to the specified sort order.
     *
     * @param articles The list of articles to filter
     * @param filter The filter criteria to apply
     * @return The filtered and sorted list of articles
     */
    private fun applyFilters(articles: List<Article>, filter: ArticleFilter): List<Article> {
        var filteredArticles = articles

        // Filter by section if specified
        filter.section?.let { section ->
            filteredArticles = filteredArticles.filter { article ->
                article.section.equals(section, ignoreCase = true)
            }
        }

        // Filter by media presence if specified
        filter.hasMedia?.let { hasMedia ->
            filteredArticles = if (hasMedia) {
                filteredArticles.filter { it.hasMedia }
            } else {
                filteredArticles.filter { !it.hasMedia }
            }
        }

        // Apply sorting
        filteredArticles = when (filter.sortBy) {
            ArticleFilter.SortBy.VIEWS_DESC -> filteredArticles.sortedByDescending { it.views }
            ArticleFilter.SortBy.VIEWS_ASC -> filteredArticles.sortedBy { it.views }
            ArticleFilter.SortBy.DATE_DESC -> filteredArticles.sortedByDescending { it.publishedDate }
            ArticleFilter.SortBy.DATE_ASC -> filteredArticles.sortedBy { it.publishedDate }
            ArticleFilter.SortBy.TITLE_ASC -> filteredArticles.sortedBy { it.title }
            ArticleFilter.SortBy.TITLE_DESC -> filteredArticles.sortedByDescending { it.title }
        }

        return filteredArticles
    }
}