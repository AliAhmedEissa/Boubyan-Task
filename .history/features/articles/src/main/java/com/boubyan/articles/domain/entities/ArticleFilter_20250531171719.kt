package com.boubyan.articles.domain.entities

data class ArticleFilter(
    val period: Period = Period.getDefault(),
    val section: String? = null,
    val hasMedia: Boolean? = null,
    val sortBy: SortBy = SortBy.VIEWS_DESC
) {
    enum class SortBy {
        VIEWS_DESC,
        VIEWS_ASC,
        DATE_DESC,
        DATE_ASC,
        TITLE_ASC,
        TITLE_DESC
    }
    
    companion object {
        fun default() = ArticleFilter()
        
        fun forPeriod(period: Period) = ArticleFilter(period = period)
        
        fun withMediaOnly() = ArticleFilter(hasMedia = true)
    }
    
    fun copyWithPeriod(newPeriod: Period): ArticleFilter {
        return copy(period = newPeriod)
    }
    
    fun copyWithSection(newSection: String?): ArticleFilter {
        return copy(section = newSection)
    }
    
    fun copyWithMediaFilter(hasMedia: Boolean?): ArticleFilter {
        return copy(hasMedia = hasMedia)
    }
    
    fun copyWithSortBy(sortBy: SortBy): ArticleFilter {
        return copy(sortBy = sortBy)
    }
}