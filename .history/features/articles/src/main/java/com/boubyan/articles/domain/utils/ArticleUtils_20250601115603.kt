package com.boubyan.articles.domain.utils

import com.boubyan.articles.domain.entities.Article

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

    fun getTotalViews(articles: List<Article>): Long {
        return articles.sumOf { it.views }
    }

    fun getAverageViews(articles: List<Article>): Double {
        return if (articles.isEmpty()) 0.0 else getTotalViews(articles).toDouble() / articles.size
    }

    fun getArticleStatistics(articles: List<Article>): ArticleStatistics {
        return ArticleStatistics(totalArticles = articles.size,
            totalViews = getTotalViews(articles),
            averageViews = getAverageViews(articles),
            articlesWithMedia = articles.count { it.hasMedia },
            sections = extractSections(articles),
            topArticle = articles.maxByOrNull { it.views },
            mostRecentArticle = articles.maxByOrNull { it.publishedDate })
    }

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