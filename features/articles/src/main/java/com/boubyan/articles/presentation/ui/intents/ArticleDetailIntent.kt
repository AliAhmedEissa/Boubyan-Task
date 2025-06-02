package com.boubyan.articles.presentation.ui.intents

/**
 * Sealed class representing all possible user intents/actions for the article detail screen.
 * Each subclass represents a specific user action that can be performed.
 */
sealed class ArticleDetailIntent {
    /**
     * Intent to load a specific article's details.
     * @param articleId The ID of the article to load
     */
    data class LoadArticle(val articleId: Long) : ArticleDetailIntent()
    
    /** Intent to refresh the current article's details */
    object RefreshArticle : ArticleDetailIntent()
    
    /** Intent to toggle the bookmark status of the current article */
    object ToggleBookmark : ArticleDetailIntent()
    
    /** Intent to share the current article */
    object ShareArticle : ArticleDetailIntent()
    
    /** Intent to open the current article in the device's browser */
    object OpenInBrowser : ArticleDetailIntent()
    
    /** Intent to retry loading the article after a failure */
    object RetryLoading : ArticleDetailIntent()
}