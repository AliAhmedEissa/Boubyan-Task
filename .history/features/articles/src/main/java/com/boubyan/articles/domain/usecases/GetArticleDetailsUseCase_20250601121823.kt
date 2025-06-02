package com.boubyan.articles.domain.usecases

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.core.base.BaseUseCase
import com.boubyan.core.base.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetArticleDetailsUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) : BaseUseCase<GetArticleDetailsUseCase.Params, Article>() {

    data class Params(
        val articleId: Long
    )

    override fun execute(parameters: Params): Flow<Resource<Article>> {
        if (parameters.articleId <= 0) {
            return flowOf(
                Resource.Error(IllegalArgumentException("Invalid article ID"))
            )
        }

        return articlesRepository.getArticleById(parameters.articleId)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isValidArticle) {
                            resource
                        } else {
                            Resource.Error(Exception("Invalid article data"))
                        }
                    }
                    is Resource.Error -> resource
                    is Resource.Loading -> resource
                }
    }
    }
}