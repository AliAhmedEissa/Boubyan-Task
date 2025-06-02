package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching articles based on a query string.
 * Implements the BaseUseCase pattern for handling article search operations.
 */
class SearchArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<SearchArticlesUseCase.Params, List<Article>>() {

    /**
     * Data class containing the parameters required for article search.
     * @param query The search query string
     */
    data class Params(val query: String)

    /**
     * Executes the article search operation.
     * Trims the search query and delegates to the repository.
     *
     * @param parameters The search parameters containing the query
     * @return Flow of Resource containing the list of matching articles
     */
    override fun execute(parameters: Params): Flow<Resource<List<Article>>> {
        return articlesRepository.searchArticles(parameters.query.trim())
    }
}