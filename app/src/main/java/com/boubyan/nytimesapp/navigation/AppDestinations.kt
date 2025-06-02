package com.boubyan.nytimesapp.navigation

/**
 * Object containing route constants for the NY Times App navigation.
 * These constants define the navigation paths used throughout the app.
 */
object AppDestinations {
    /** Route for the articles list screen */
    const val ARTICLES = "articles"
    /** Route for the article detail screen with article ID parameter */
    const val ARTICLE_DETAIL = "article_detail/{articleId}"

    /**
     * Creates the article detail route with the specified article ID.
     *
     * @param articleId The ID of the article to display
     * @return The complete route string for the article detail screen
     */
    fun articleDetail(articleId: Long): String {
        return "article_detail/$articleId"
    }
}

/**
 * Sealed class representing the app's navigation routes.
 * Each route is represented by a specific class that extends this sealed class.
 */
sealed class AppRoute(val route: String) {
    /** Route for the articles list screen */
    object Articles : AppRoute(AppDestinations.ARTICLES)

    /**
     * Route for the article detail screen.
     * @property articleId The ID of the article to display
     */
    data class ArticleDetail(val articleId: Long) : AppRoute(AppDestinations.articleDetail(articleId))
}