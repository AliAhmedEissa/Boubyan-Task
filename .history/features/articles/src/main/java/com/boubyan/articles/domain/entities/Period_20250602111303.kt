package com.boubyan.articles.domain.entities

/**
 * Enum class representing different time periods for article filtering.
 * Each period has a value used in API calls and a display name for UI.
 */
enum class Period(val value: String, val displayName: String) {
    /** Represents articles from the last 24 hours */
    ONE_DAY("1", "Last Day"),
    /** Represents articles from the last 7 days */
    SEVEN_DAYS("7", "Last Week"),
    /** Represents articles from the last 30 days */
    THIRTY_DAYS("30", "Last Month");

    companion object {
        /**
         * Creates a Period instance from a string value.
         * If the value doesn't match any period, returns SEVEN_DAYS as default.
         *
         * @param value The period value to convert
         * @return The corresponding Period enum value
         */
        fun fromValue(value: String): Period {
            return values().find { it.value == value } ?: SEVEN_DAYS
        }

        /**
         * Returns the default period (SEVEN_DAYS).
         *
         * @return The default Period value
         */
        fun getDefault(): Period = SEVEN_DAYS

        /**
         * Returns a list of all available periods.
         *
         * @return List of all Period values
         */
        fun getAllPeriods(): List<Period> = values().toList()
    }

    /**
     * Returns true if this period is the shortest (ONE_DAY).
     */
    val isShortPeriod: Boolean
        get() = this == ONE_DAY

    /**
     * Returns true if this period is the longest (THIRTY_DAYS).
     */
    val isLongPeriod: Boolean
        get() = this == THIRTY_DAYS
}