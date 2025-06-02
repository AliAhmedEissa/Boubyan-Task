package com.boubyan.core.error

/**
 * Sealed class representing application-specific exceptions.
 * Each subclass represents a specific type of error that can occur in the app.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    /** Represents network-related errors */
    class NetworkException(message: String) : AppException(message)
    
    /** Represents unauthorized access errors (401) */
    class UnauthorizedException(message: String) : AppException(message)
    
    /** Represents forbidden access errors (403) */
    class ForbiddenException(message: String) : AppException(message)
    
    /** Represents resource not found errors (404) */
    class NotFoundException(message: String) : AppException(message)
    
    /** Represents rate limiting errors (429) */
    class RateLimitException(message: String) : AppException(message)
    
    /** Represents server-side errors (5xx) */
    class ServerException(message: String) : AppException(message)
    
    /** Represents unknown errors */
    class UnknownException(message: String) : AppException(message)
    
    /** Represents validation errors */
    class ValidationException(message: String) : AppException(message)
    
    /** Represents data parsing errors */
    class ParseException(message: String, cause: Throwable? = null) : AppException(message, cause)
}