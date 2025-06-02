package com.boubyan.articles.domain.entities

enum class Period(val value: String, val displayName: String) {
    ONE_DAY("1", "Last Day"),
    SEVEN_DAYS("7", "Last Week"),
    THIRTY_DAYS("30", "Last Month");

    companion object {
        fun fromValue(value: String): Period {
            return values().find { it.value == value } ?: SEVEN_DAYS
        }

        fun getDefault(): Period = SEVEN_DAYS

        fun getAllPeriods(): List<Period> = values().toList()
    }

    val isShortPeriod: Boolean
        get() = this == ONE_DAY

    val isLongPeriod: Boolean
        get() = this == THIRTY_DAYS
}