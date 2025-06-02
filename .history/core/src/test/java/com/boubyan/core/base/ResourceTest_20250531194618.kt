package com.boubyan.core.base

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceTest {

    @Test
    fun `Resource Success should hold data correctly`() {
        val data = "test data"
        val resource = Resource.Success(data)

        assertEquals(data, resource.data)
        assertTrue(resource.isSuccess)
        assertFalse(resource.isLoading)
        assertFalse(resource.isError)
    }

    @Test
    fun `Resource Error should hold exception correctly`() {
        val exception = Exception("Test exception")
        val resource = Resource.Error(exception)

        assertEquals(exception, resource.exception)
        assertTrue(resource.isError)
        assertFalse(resource.isLoading)
        assertFalse(resource.isSuccess)
    }

    @Test
    fun `Resource Loading should have correct state`() {
        val resource = Resource.Loading

        assertTrue(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertFalse(resource.isError)
    }

    @Test
    fun `onSuccess should execute action when Success`() {
        var executed = false
        val resource = Resource.Success("data")

        resource.onSuccess { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onSuccess should not execute action when Error`() {
        var executed = false
        val resource = Resource.Error(Exception())

        resource.onSuccess { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `onError should execute action when Error`() {
        var executed = false
        val exception = Exception("test")
        val resource = Resource.Error(exception)

        resource.onError { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `onError should not execute action when Success`() {
        var executed = false
        val resource = Resource.Success("data")

        resource.onError { executed = true }

        assertFalse(executed)
    }

    @Test
    fun `onLoading should execute action when Loading`() {
        var executed = false
        val resource = Resource.Loading

        resource.onLoading { executed = true }

        assertTrue(executed)
    }

    @Test
    fun `test Loading state`() {
        // When
        val resource = Resource.Loading

        // Then
        assertTrue(resource is Resource.Loading)
    }

    @Test
    fun `test Success state with data`() {
        // Given
        val testData = "test data"

        // When
        val resource = Resource.Success(testData)

        // Then
        assertTrue(resource is Resource.Success)
        assertEquals(testData, resource.data)
    }

    @Test
    fun `test Error state with exception`() {
        // Given
        val exception = Exception("test error")

        // When
        val resource = Resource.Error(exception)

        // Then
        assertTrue(resource is Resource.Error)
        assertEquals(exception, resource.exception)
    }

    @Test
    fun `test Resource fold with Loading`() {
        // Given
        val resource = Resource.Loading
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false

        // When
        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { successCalled = true },
            onError = { errorCalled = true }
        )

        // Then
        assertTrue(loadingCalled)
        assertFalse(successCalled)
        assertFalse(errorCalled)
    }

    @Test
    fun `test Resource fold with Success`() {
        // Given
        val testData = "test data"
        val resource = Resource.Success(testData)
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false
        var receivedData: String? = null

        // When
        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { data ->
                successCalled = true
                receivedData = data
            },
            onError = { errorCalled = true }
        )

        // Then
        assertFalse(loadingCalled)
        assertTrue(successCalled)
        assertFalse(errorCalled)
        assertEquals(testData, receivedData)
    }

    @Test
    fun `test Resource fold with Error`() {
        // Given
        val exception = Exception("test error")
        val resource = Resource.Error(exception)
        var loadingCalled = false
        var successCalled = false
        var errorCalled = false
        var receivedException: Exception? = null

        // When
        resource.fold(
            onLoading = { loadingCalled = true },
            onSuccess = { successCalled = true },
            onError = { exception ->
                errorCalled = true
                receivedException = exception
            }
        )

        // Then
        assertFalse(loadingCalled)
        assertFalse(successCalled)
        assertTrue(errorCalled)
        assertEquals(exception, receivedException)
    }
}