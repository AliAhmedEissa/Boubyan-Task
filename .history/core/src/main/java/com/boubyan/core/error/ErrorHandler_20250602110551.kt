package com.boubyan.core.error

import android.content.Context
import com.boubyan.core.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Class responsible for handling and formatting error messages.
 * Provides user-friendly error messages and logging functionality.
 */
@Singleton
class ErrorHandler @Inject constructor(
    private val context: Context
) {
    
    /**
     * Gets a user-friendly error message for an exception.
     * Maps different types of exceptions to appropriate string resources.
     *
     * @param exception The exception to get a message for
     * @return A user-friendly error message
     */
    fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is AppException.NetworkException -> context.getString(R.string.error_network)
            is AppException.UnauthorizedException -> context.getString(R.string.error_unauthorized)
            is AppException.ForbiddenException -> context.getString(R.string.error_forbidden)
            is AppException.NotFoundException -> context.getString(R.string.error_not_found)
            is AppException.RateLimitException -> context.getString(R.string.error_rate_limit)
            is AppException.ServerException -> context.getString(R.string.error_server)
            is AppException.ValidationException -> exception.message ?: context.getString(R.string.error_validation)
            is AppException.ParseException -> context.getString(R.string.error_parse)
            else -> context.getString(R.string.error_unknown)
        }
    }
    
    /**
     * Logs an error with the specified tag.
     * Currently uses Android's Log.e, but can be extended to use a proper
     * logging framework or crash reporting service.
     *
     * @param exception The exception to log
     * @param tag The tag to use for logging (defaults to "ErrorHandler")
     */
    fun logError(exception: Throwable, tag: String = "ErrorHandler") {
        // In production, use proper logging framework like Timber or send to crash reporting
        android.util.Log.e(tag, "Error occurred", exception)
    }
}