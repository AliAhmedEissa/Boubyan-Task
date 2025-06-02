package com.boubyan.core.error

import android.content.Context
import com.boubyan.core.R
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ErrorHandlerTest {

    @Mock
    private lateinit var context: Context

    private lateinit var errorHandler: ErrorHandler

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        errorHandler = ErrorHandler(context)
    }

    @Test
    fun `getErrorMessage should return network error message for NetworkException`() {
        val expectedMessage = "Network error"
        whenever(context.getString(R.string.error_network)).thenReturn(expectedMessage)

        val result = errorHandler.getErrorMessage(AppException.NetworkException("test"))

        assertEquals(expectedMessage, result)
    }

    @Test
    fun `getErrorMessage should return unauthorized error message for UnauthorizedException`() {
        val expectedMessage = "Unauthorized error"
        whenever(context.getString(R.string.error_unauthorized)).thenReturn(expectedMessage)

        val result = errorHandler.getErrorMessage(AppException.UnauthorizedException("test"))

        assertEquals(expectedMessage, result)
    }

    @Test
    fun `getErrorMessage should return server error message for ServerException`() {
        val expectedMessage = "Server error"
        whenever(context.getString(R.string.error_server)).thenReturn(expectedMessage)

        val result = errorHandler.getErrorMessage(AppException.ServerException("test"))

        assertEquals(expectedMessage, result)
    }

    @Test
    fun `getErrorMessage should return unknown error message for unknown exception`() {
        val expectedMessage = "Unknown error"
        whenever(context.getString(R.string.error_unknown)).thenReturn(expectedMessage)

        val result = errorHandler.getErrorMessage(RuntimeException("test"))

        assertEquals(expectedMessage, result)
    }
}