package com.boubyan.articles.presentation.ui.states

import com.boubyan.articles.presentation.models.ArticleUiModel

data class ArticleDetailState(
    val isLoading: Boolean = false,
    val article: ArticleUiModel? = null,
    val error: String? = null,
    val isBookmarked: Boolean = false
) {
    val hasError: Boolean
        get() = error != null && !isLoading
        
    val showContent: Boolean
        get() = article != null && !isLoading
}