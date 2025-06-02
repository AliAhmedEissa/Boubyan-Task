package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for retrieving detailed information about a specific article.
 * Implements the BaseUseCase pattern for handling article detail retrieval operations.
 */
class GetArticleDetailsUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<GetArticleDetailsUseCase.Params, Article>() {

    /**
     * Data class containing the parameters required for fetching article details.
     * @param articleId The unique identifier of the article to retrieve
     */
    data class Params(
        val articleId: String
    )

    /**
     * Executes the article detail retrieval operation.
     * Validates the article ID and fetches the article details from the repository.
     *
     * @param parameters The parameters containing the article ID
     * @return Flow of Resource containing the article details
     * @throws IllegalArgumentException if the article ID is empty
     */
    override fun execute(parameters: Params): Flow<Resource<Article>> {
        if (parameters.articleId.isBlank()) {
            throw IllegalArgumentException("Article ID cannot be empty")
        }
        return articlesRepository.getArticleDetails(parameters.articleId)
    }
}