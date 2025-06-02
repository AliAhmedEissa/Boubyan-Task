package com.boubyan.articles.presentation.ui.intents

import com.boubyan.articles.domain.entities.Period

sealed class ArticlesListIntent {
    object LoadArticles : ArticlesListIntent()
    object RefreshArticles : ArticlesListIntent()
    data class SelectPeriod(val period: Period) : ArticlesListIntent()
    data class SearchArticles(val query: String) : ArticlesListIntent()
    object ClearSearch : ArticlesListIntent()
    object RetryLoading : ArticlesListIntent()
}