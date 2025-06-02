package com.boubyan.articles.presentation.mappers

import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.presentation.models.ArticleUiModel
import com.boubyan.core.utils.DateUtils
import javax.inject.Inject
import javax.inject.Singleton
import java.text.NumberFormat
import java.util.*

@Singleton
class ArticleUiMapper @Inject constructor() {
    
    private val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    
    fun mapToUiModel(article: Article): ArticleUiModel {
        return ArticleUiModel(
            id = article.id,
            title = article.title,
            abstract = article.abstract,
            byline = article.formattedByline,
            section = article.section,
            publishedDate = article.publishedDate,
            views = article.views.toString(),
            imageUrl = article.primaryImageUrl,
            url = article.url,
            hasMedia = article.hasMedia,
            formattedDate = DateUtils.formatDateForDisplay(article.publishedDate),
            viewsText = formatViews(article.views),
            sectionTag = article.section.uppercase()
        )
    }
    
    fun mapToUiModelList(articles: List<Article>): List<ArticleUiModel> {
        return articles.map { mapToUiModel(it) }
    }
    
    private fun formatViews(views: Long): String {
        return when {
            views >= 1_000_000 -> "${numberFormat.format(views / 1_000_000)}M views"
            views >= 1_000 -> "${numberFormat.format(views / 1_000)}K views"
            else -> "${numberFormat.format(views)} views"
        }
    }
}