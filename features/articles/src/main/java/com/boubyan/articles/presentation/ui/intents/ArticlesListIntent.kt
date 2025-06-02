package com.boubyan.articles.presentation.ui.intents

import com.boubyan.articles.domain.entities.Period

/**
 * Sealed class representing all possible user intents/actions for the articles list screen.
 * Each subclass represents a specific user action that can be performed.
 */
sealed class ArticlesListIntent {
    /** Intent to load the initial list of articles */
    object LoadArticles : ArticlesListIntent()
    
    /** Intent to refresh the current list of articles */
    object RefreshArticles : ArticlesListIntent()
    
    /**
     * Intent to change the selected time period for filtering articles.
     * @param period The new period to filter articles by
     */
    data class SelectPeriod(val period: Period) : ArticlesListIntent()
    
    /**
     * Intent to search articles with the given query.
     * @param query The search query text
     */
    data class SearchArticles(val query: String) : ArticlesListIntent()
    
    /** Intent to clear the current search query and results */
    object ClearSearch : ArticlesListIntent()
    
    /** Intent to retry loading articles after a failure */
    object RetryLoading : ArticlesListIntent()
}