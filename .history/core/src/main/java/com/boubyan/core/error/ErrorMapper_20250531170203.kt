package com.boubyan.core.error

import com.boubyan.core.base.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorMapper @Inject constructor(
    private val errorHandler: ErrorHandler
) {
    
    fun <T> mapErrorToResource(exception: Throwable): Resource.Error {
        errorHandler.logError(exception)
        return Resource.Error(exception)
    }
    
    fun getUserFriendlyMessage(exception: Throwable): String {
        return errorHandler.getErrorMessage(exception)
    }
}