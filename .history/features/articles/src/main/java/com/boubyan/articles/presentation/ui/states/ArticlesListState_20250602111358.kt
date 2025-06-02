package com.boubyan.articles.presentation.ui.states

import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.presentation.models.ArticleUiModel
import com.boubyan.articles.presentation.models.PeriodUiModel

/**
 * Data class representing the UI state for the articles list screen.
 * Contains all the necessary data and flags to render the articles list UI.
 */
data class ArticlesListState(
    /** Flag indicating if articles are currently being loaded */
    val isLoading: Boolean = false,
    /** List of articles to display */
    val articles: List<ArticleUiModel> = emptyList(),
    /** Error message to display, if any */
    val error: String? = null,
    /** Currently selected time period for filtering articles */
    val selectedPeriod: Period = Period.getDefault(),
    /** List of available time periods for filtering */
    val availablePeriods: List<PeriodUiModel> = emptyList(),
    /** Flag indicating if the list is currently being refreshed */
    val isRefreshing: Boolean = false,
    /** Current search query text */
    val searchQuery: String = "",
    /** Flag indicating if a search operation is in progress */
    val isSearching: Boolean = false
) {
    /**
     * Returns true if there are no articles to display and the list is not loading.
     */
    val isEmpty: Boolean
        get() = articles.isEmpty() && !isLoading
        
    /**
     * Returns true if there is an error to display and the list is not loading.
     */
    val hasError: Boolean
        get() = error != null && !isLoading
        
    /**
     * Returns true if there are articles to display and the list is not loading.
     */
    val showContent: Boolean
        get() = articles.isNotEmpty() && !isLoading
}