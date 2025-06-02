package com.boubyan.core.base

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
}