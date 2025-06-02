package com.boubyan.nytimesapp.navigation

object AppDestinations {
    const val ARTICLES = "articles"
    const val ARTICLE_DETAIL = "article_detail/{articleId}"

    fun articleDetail(articleId: Long): String {
        return "article_detail/$articleId"
    }
}

sealed class AppRoute(val route: String) {
    object Articles : AppRoute(AppDestinations.ARTICLES)

    data class ArticleDetail(val articleId: Long) : AppRoute(AppDestinations.articleDetail(articleId))
}