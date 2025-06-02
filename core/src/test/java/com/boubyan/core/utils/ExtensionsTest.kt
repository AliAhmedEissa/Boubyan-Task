/*
package com.boubyan.core.utils

import app.cash.turbine.test
import com.boubyan.core.base.Resource
import com.boubyan.core.error.AppException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `asResource should convert flow to Resource Success`() = runTest {
        val data = "test data"
        val flow = flowOf(data)

        flow.asResource().test {
            val result = awaitItem()
            assertTrue(result is Resource.Success)
            assertEquals(data, result.data)
            awaitComplete()
        }
    }

    @Test
    fun `mapResource should transform Success data`() = runTest {
        val originalData = "test"
        val transformedData = "TEST"
        val flow = flowOf(Resource.Success(originalData))

        flow.mapResource { it.uppercase() }.test {
            val result = awaitItem()
            assertTrue(result is Resource.Success)
            assertEquals(transformedData, result.data)
            awaitComplete()
        }
    }

    @Test
    fun `mapResource should pass through Error unchanged`() = runTest {
        val exception = Exception("test error")
        val flow = flowOf(Resource.Error(exception))

        flow.mapResource { it.toString() }.test {
            val result = awaitItem()
            assertTrue(result is Resource.Error)
            assertEquals(exception, result.exception)
            awaitComplete()
        }
    }

    @Test
    fun `orEmpty should return empty string for null`() {
        val nullString: String? = null
        assertEquals("", nullString.orEmpty())
    }

    @Test
    fun `orEmpty should return original string for non-null`() {
        val originalString = "test"
        assertEquals(originalString, originalString.orEmpty())
    }

    @Test
    fun `orDefault should return default for null or blank`() {
        val nullString: String? = null
        val blankString = "   "
        val defaultValue = "default"

        assertEquals(defaultValue, nullString.orDefault(defaultValue))
        assertEquals(defaultValue, blankString.orDefault(defaultValue))
    }

    @Test
    fun `orDefault should return original for non-blank`() {
        val originalString = "test"
        val defaultValue = "default"

        assertEquals(originalString, originalString.orDefault(defaultValue))
    }

    @Test
    fun `toAppException should convert unknown exception to AppException`() {
        val originalException = RuntimeException("test")
        val appException = originalException.toAppException()

        assertTrue(appException is AppException.UnknownException)
    }

    @Test
    fun `toAppException should return same AppException`() {
        val appException = AppException.NetworkException("test")
        val result = appException.toAppException()

        assertEquals(appException, result)
    }
}*/
