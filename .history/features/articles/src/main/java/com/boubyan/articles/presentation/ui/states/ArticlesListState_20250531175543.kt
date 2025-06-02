package com.boubyan.articles.presentation.ui.states

import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.presentation.models.ArticleUiModel
import com.boubyan.articles.presentation.models.PeriodUiModel

data class ArticlesListState(
    val isLoading: Boolean = false,
    val articles: List<ArticleUiModel> = emptyList(),
    val error: String? = null,
    val selectedPeriod: Period = Period.getDefault(),
    val availablePeriods: List<PeriodUiModel> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val isSearching: Boolean = false
) {
    val isEmpty: Boolean
        get() = articles.isEmpty() && !isLoading
        
    val hasError: Boolean
        get() = error != null && !isLoading
        
    val showContent: Boolean
        get() = articles.isNotEmpty() && !isLoading
}