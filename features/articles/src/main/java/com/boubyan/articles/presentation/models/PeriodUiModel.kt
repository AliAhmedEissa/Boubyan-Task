package com.boubyan.articles.presentation.models

data class PeriodUiModel(
    val value: String,
    val displayName: String,
    val isSelected: Boolean = false
) {
    companion object {
        fun from(periods: List<com.boubyan.articles.domain.entities.Period>, selectedValue: String) = 
            periods.map { period ->
                PeriodUiModel(
                    value = period.value,
                    displayName = period.displayName,
                    isSelected = period.value == selectedValue
                )
            }
    }
}