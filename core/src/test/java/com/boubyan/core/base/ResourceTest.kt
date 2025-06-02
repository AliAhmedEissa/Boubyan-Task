/*
package com.boubyan.core.base

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceTest {

    @Test
    fun `test Loading state properties`() {
        val resource = Resource.Loading

        assertTrue(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertFalse(resource.isError)
        assertTrue(resource.data == null)
        assertTrue(resource.exception == null)
    }

    @Test
    fun `test Success state properties`() {
        val testData = "test data"
        val resource = Resource.Success(testData)

        assertTrue(resource.isSuccess)
        assertFalse(resource.isLoading)
        assertFalse(resource.isError)
        assertEquals(testData, resource.data)
        assertTrue(resource.exception == null)
    }

    @Test
    fun `test Error state properties`() {
        val exception = Exception("test error")
        val resource = Resource.Error(exception)

        assertTrue(resource.isError)
        assertFalse(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertTrue(resource.data == null)
        assertEquals(exception, resource.exception)
    }

    @Test
    fun `test fold with Loading state`() {
        val resource = Resource.Loading
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false

        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { successCalled = true },
            onError = { errorCalled = true }
        )

        assertTrue(loadingCalled)
        assertFalse(successCalled)
        assertFalse(errorCalled)
    }

    @Test
    fun `test fold with Success state`() {
        val testData = "test data"
        val resource = Resource.Success(testData)
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false
        var receivedData: String? = null

        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { data ->
                successCalled = true
                receivedData = data
            },
            onError = { errorCalled = true }
        )

        assertFalse(loadingCalled)
        assertTrue(successCalled)
        assertFalse(errorCalled)
        assertEquals(testData, receivedData)
    }

    @Test
    fun `test fold with Error state`() {
        val exception = Exception("test error")
        val resource = Resource.Error(exception)
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false
        var receivedException: Exception? = null

        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { successCalled = true },
            onError = { exception ->
                errorCalled = true
                receivedException = exception
            }
        )

        assertFalse(loadingCalled)
        assertFalse(successCalled)
        assertTrue(errorCalled)
        assertEquals(exception, receivedException)
    }

    @Test
    fun `test onSuccess callback with Success state`() {
        val testData = "test data"
        val resource = Resource.Success(testData)
        var callbackCalled = false
        var receivedData: String? = null

        resource.onSuccess { data ->
            callbackCalled = true
            receivedData = data
        }

        assertTrue(callbackCalled)
        assertEquals(testData, receivedData)
    }

    @Test
    fun `test onSuccess callback with non-Success state`() {
        val resource = Resource.Loading
        var callbackCalled = false

        resource.onSuccess { callbackCalled = true }

        assertFalse(callbackCalled)
    }

    @Test
    fun `test onError callback with Error state`() {
        val exception = Exception("test error")
        val resource = Resource.Error(exception)
        var callbackCalled = false
        var receivedException: Exception? = null

        resource.onError { exception ->
            callbackCalled = true
            receivedException = exception
        }

        assertTrue(callbackCalled)
        assertEquals(exception, receivedException)
    }

    @Test
    fun `test onError callback with non-Error state`() {
        val resource = Resource.Loading
        var callbackCalled = false

        resource.onError { callbackCalled = true }

        assertFalse(callbackCalled)
    }

    @Test
    fun `test onLoading callback with Loading state`() {
        val resource = Resource.Loading
        var callbackCalled = false

        resource.onLoading { callbackCalled = true }

        assertTrue(callbackCalled)
    }

    @Test
    fun `test onLoading callback with non-Loading state`() {
        val resource = Resource.Success("test")
        var callbackCalled = false

        resource.onLoading { callbackCalled = true }

        assertFalse(callbackCalled)
    }
} */
