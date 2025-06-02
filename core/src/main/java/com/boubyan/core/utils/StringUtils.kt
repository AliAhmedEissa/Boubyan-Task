package com.boubyan.core.utils

object StringUtils {
    
    fun isNullOrEmpty(value: String?): Boolean {
        return value.isNullOrEmpty()
    }
    
    fun isNullOrBlank(value: String?): Boolean {
        return value.isNullOrBlank()
    }
    
    fun capitalize(value: String?): String {
        return value?.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase() else it.toString() 
        } ?: ""
    }
    
    fun truncate(value: String?, maxLength: Int): String {
        if (value.isNullOrEmpty()) return ""
        return if (value.length > maxLength) {
            "${value.substring(0, maxLength)}..."
        } else {
            value
        }
    }
    
    fun removeHtmlTags(value: String?): String {
        return value?.replace(Regex("<[^>]*>"), "") ?: ""
    }
    
    fun extractDomain(url: String?): String {
        if (url.isNullOrEmpty()) return ""
        
        return try {
            val domain = url.replace(Regex("^https?://"), "")
                .replace(Regex("^www\\."), "")
                .split("/")[0]
            domain
        } catch (e: Exception) {
            ""
        }
    }
}