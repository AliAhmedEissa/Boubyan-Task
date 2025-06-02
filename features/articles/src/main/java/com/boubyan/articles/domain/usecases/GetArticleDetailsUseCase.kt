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
 * Use case responsible for retrieving detailed information about a specific article.
 *
 * This use case follows the Clean Architecture pattern and handles the business logic
 * for fetching article details, including input validation and data verification.
 *
 * @param articlesRepository Repository interface for accessing article data
 */
class GetArticleDetailsUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<GetArticleDetailsUseCase.Params, Article>() {

    /**
     * Parameters required for executing this use case.
     *
     * @param articleId The unique identifier of the article to retrieve
     */
    data class Params(
        val articleId: Long
    )

    /**
     * Executes the use case to retrieve article details.
     *
     * This method performs the following operations:
     * 1. Validates the input article ID
     * 2. Fetches the article from the repository
     * 3. Validates the retrieved article data
     * 4. Returns appropriate Resource states (Success, Error, Loading)
     *
     * @param parameters Contains the article ID to fetch
     * @return Flow<Resource<Article>> A reactive stream that emits the article data
     *         wrapped in a Resource state (Success, Error, or Loading)
     */
    override fun execute(parameters: Params): Flow<Resource<Article>> {
        // Input validation: Check if article ID is valid (greater than 0)
        if (parameters.articleId <= 0) {
            return flowOf(
                Resource.Error(IllegalArgumentException("Invalid article ID"))
            )
        }

        // Fetch article from repository and transform the result
        return articlesRepository.getArticleById(parameters.articleId)
            .map { resource ->
                when (resource) {
                    // Handle successful data retrieval
                    is Resource.Success -> {
                        // Validate the article data using the entity's validation logic
                        if (resource.data.isValidArticle) {
                            resource // Return the successful resource as-is
                        } else {
                            // Convert to error if article data is invalid
                            Resource.Error(Exception("Invalid article data"))
                        }
                    }
                    // Pass through error states unchanged
                    is Resource.Error -> resource
                    // Pass through loading states unchanged
                    is Resource.Loading -> resource
                }
            }
    }
}
