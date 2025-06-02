package com.boubyan.articles.presentation.ui.intents

sealed class ArticleDetailIntent {
    data class LoadArticle(val articleId: Long) : ArticleDetailIntent()
    object RefreshArticle : ArticleDetailIntent()
    object ToggleBookmark : ArticleDetailIntent()
    object ShareArticle : ArticleDetailIntent()
    object OpenInBrowser : ArticleDetailIntent()
    object RetryLoading : ArticleDetailIntent()
}