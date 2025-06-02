package com.boubyan.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private const val API_DATE_FORMAT = "yyyy-MM-dd"
    private const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
    private const val FULL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    
    private val apiDateFormatter = SimpleDateFormat(API_DATE_FORMAT, Locale.US)
    private val displayDateFormatter = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.US)
    private val fullDateFormatter = SimpleDateFormat(FULL_DATE_FORMAT, Locale.US)
    
    fun formatDateForDisplay(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""
        
        return try {
            val date = apiDateFormatter.parse(dateString)
            date?.let { displayDateFormatter.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    fun formatFullDateForDisplay(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""
        
        return try {
            val date = fullDateFormatter.parse(dateString)
            date?.let { displayDateFormatter.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    
    fun getCurrentDate(): String {
        return apiDateFormatter.format(Date())
    }
    
    fun getDateDaysAgo(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return apiDateFormatter.format(calendar.time)
    }
}