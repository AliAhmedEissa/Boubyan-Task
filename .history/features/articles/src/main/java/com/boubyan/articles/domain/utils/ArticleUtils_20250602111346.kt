package com.boubyan.articles.domain.utils

import com.boubyan.articles.domain.entities.Article

/**
 * Utility object providing helper functions for article-related calculations and statistics.
 */
object ArticleUtils {

    fun extractSections(articles: List<Article>): List<String> {
        return articles.map { it.section }.filter { it.isNotBlank() }.distinct().sorted()
    }

    fun getArticlesByMediaType(articles: List<Article>, hasMedia: Boolean): List<Article> {
        return if (hasMedia) {
            articles.filter { it.hasMedia }
        } else {
            articles.filter { !it.hasMedia }
        }
    }

    fun getTopArticlesByViews(articles: List<Article>, limit: Int = 10): List<Article> {
        return articles.sortedByDescending { it.views }.take(limit)
    }

    fun getRecentArticles(articles: List<Article>, limit: Int = 10): List<Article> {
        return articles.sortedByDescending { it.publishedDate }.take(limit)
    }

    fun searchInArticles(articles: List<Article>, query: String): List<Article> {
        val queryLower = query.lowercase()
        return articles.filter { article ->
            article.title.lowercase().contains(queryLower) || article.abstract.lowercase()
                .contains(queryLower) || article.section.lowercase()
                .contains(queryLower) || article.byline.lowercase().contains(queryLower)
        }
    }

    fun groupArticlesBySection(articles: List<Article>): Map<String, List<Article>> {
        return articles.groupBy { it.section }
    }

    fun getArticleById(articles: List<Article>,id: Long):Article? {
        return articles.find { it.id == id }
    }

    /**
     * Calculates the total number of views across all articles.
     *
     * @param articles List of articles to calculate total views from
     * @return The sum of views across all articles
     */
    fun getTotalViews(articles: List<Article>): Long {
        return articles.sumOf { it.views }
    }

    /**
     * Calculates the average number of views per article.
     * Returns 0.0 if the article list is empty.
     *
     * @param articles List of articles to calculate average views from
     * @return The average number of views per article
     */
    fun getAverageViews(articles: List<Article>): Double {
        return if (articles.isEmpty()) 0.0 else getTotalViews(articles).toDouble() / articles.size
    }

    /**
     * Generates comprehensive statistics about a list of articles.
     * Includes total articles, views, averages, media presence, sections, and notable articles.
     *
     * @param articles List of articles to generate statistics from
     * @return An ArticleStatistics object containing the calculated statistics
     */
    fun getArticleStatistics(articles: List<Article>): ArticleStatistics {
        return ArticleStatistics(totalArticles = articles.size,
            totalViews = getTotalViews(articles),
            averageViews = getAverageViews(articles),
            articlesWithMedia = articles.count { it.hasMedia },
            sections = extractSections(articles),
            topArticle = articles.maxByOrNull { it.views },
            mostRecentArticle = articles.maxByOrNull { it.publishedDate })
    }

    /**
     * Data class containing statistical information about a collection of articles.
     *
     * @property totalArticles Total number of articles
     * @property totalViews Total number of views across all articles
     * @property averageViews Average number of views per article
     * @property articlesWithMedia Number of articles that contain media
     * @property sections List of unique section names
     * @property topArticle The article with the highest view count
     * @property mostRecentArticle The most recently published article
     */
    data class ArticleStatistics(
        val totalArticles: Int,
        val totalViews: Long,
        val averageViews: Double,
        val articlesWithMedia: Int,
        val sections: List<String>,
        val topArticle: Article?,
        val mostRecentArticle: Article?
    )
}