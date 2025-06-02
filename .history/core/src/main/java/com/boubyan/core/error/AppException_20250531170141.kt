package com.boubyan.core.error

sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    class NetworkException(message: String) : AppException(message)
    
    class UnauthorizedException(message: String) : AppException(message)
    
    class ForbiddenException(message: String) : AppException(message)
    
    class NotFoundException(message: String) : AppException(message)
    
    class RateLimitException(message: String) : AppException(message)
    
    class ServerException(message: String) : AppException(message)
    
    class UnknownException(message: String) : AppException(message)
    
    class ValidationException(message: String) : AppException(message)
    
    class ParseException(message: String, cause: Throwable? = null) : AppException(message, cause)
}