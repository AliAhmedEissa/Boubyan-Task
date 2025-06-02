package com.boubyan.articles.domain.entities

/**
 * Data class representing filtering options for articles.
 * Allows filtering by period, section, media presence, and sorting order.
 */
data class ArticleFilter(
    /** The time period for which to fetch articles */
    val period: Period = Period.getDefault(),
    /** Optional section name to filter articles by */
    val section: String? = null,
    /** Optional flag to filter articles with/without media */
    val hasMedia: Boolean? = null,
    /** The sorting order for the articles */
    val sortBy: SortBy = SortBy.VIEWS_DESC
) {
    /**
     * Enum class defining the available sorting options for articles.
     */
    enum class SortBy {
        /** Sort by views in descending order (most viewed first) */
        VIEWS_DESC,
        /** Sort by views in ascending order (least viewed first) */
        VIEWS_ASC,
        /** Sort by date in descending order (newest first) */
        DATE_DESC,
        /** Sort by date in ascending order (oldest first) */
        DATE_ASC,
        /** Sort by title in ascending order (A-Z) */
        TITLE_ASC,
        /** Sort by title in descending order (Z-A) */
        TITLE_DESC
    }
    
    companion object {
        /**
         * Creates a default ArticleFilter with default values.
         *
         * @return A default ArticleFilter instance
         */
        fun default() = ArticleFilter()
        
        /**
         * Creates an ArticleFilter with a specific period.
         *
         * @param period The period to filter by
         * @return An ArticleFilter instance with the specified period
         */
        fun forPeriod(period: Period) = ArticleFilter(period = period)
        
        /**
         * Creates an ArticleFilter that only includes articles with media.
         *
         * @return An ArticleFilter instance that filters for articles with media
         */
        fun withMediaOnly() = ArticleFilter(hasMedia = true)
    }
    
    /**
     * Creates a copy of this filter with a new period.
     *
     * @param newPeriod The new period to use
     * @return A new ArticleFilter instance with the updated period
     */
    fun copyWithPeriod(newPeriod: Period): ArticleFilter {
        return copy(period = newPeriod)
    }
    
    /**
     * Creates a copy of this filter with a new section.
     *
     * @param newSection The new section to filter by
     * @return A new ArticleFilter instance with the updated section
     */
    fun copyWithSection(newSection: String?): ArticleFilter {
        return copy(section = newSection)
    }
    
    /**
     * Creates a copy of this filter with a new media filter.
     *
     * @param hasMedia The new media filter value
     * @return A new ArticleFilter instance with the updated media filter
     */
    fun copyWithMediaFilter(hasMedia: Boolean?): ArticleFilter {
        return copy(hasMedia = hasMedia)
    }
    
    /**
     * Creates a copy of this filter with a new sort order.
     *
     * @param sortBy The new sort order to use
     * @return A new ArticleFilter instance with the updated sort order
     */
    fun copyWithSortBy(sortBy: SortBy): ArticleFilter {
        return copy(sortBy = sortBy)
    }
}