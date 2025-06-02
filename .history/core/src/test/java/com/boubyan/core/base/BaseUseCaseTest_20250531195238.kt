package com.boubyan.core.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful execution with params`() = runTest {
        // Given
        val useCase = TestUseCase()
        val params = TestParams("test")

        // When
        val result = useCase(params).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("test", (result as Resource.Success).data)
    }

    @Test
    fun `test successful execution without params`() = runTest {
        // Given
        val useCase = NoParamsUseCase()

        // When
        val result = useCase().first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("default", (result as Resource.Success).data)
    }

    @Test
    fun `test error execution with exception`() = runTest {
        // Given
        val useCase = TestUseCase(shouldThrow = true)
        val params = TestParams("test")

        // When
        val result = useCase(params).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Test error", (result as Resource.Error).exception.message)
    }

    @Test
    fun `test error execution with custom exception`() = runTest {
        // Given
        val useCase = TestUseCase(shouldThrow = true, customException = true)
        val params = TestParams("test")

        // When
        val result = useCase(params).first()

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).exception is CustomException)
        assertEquals("Custom error", result.exception.message)
    }

    @Test
    fun `test loading state during execution`() = runTest {
        // Given
        val useCase = DelayedUseCase()
        val params = TestParams("test")

        // When
        val result = useCase(params).first()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("delayed", (result as Resource.Success).data)
    }

    private data class TestParams(val value: String)

    private class TestUseCase(
        private val shouldThrow: Boolean = false,
        private val customException: Boolean = false
    ) : BaseUseCase<TestParams, String>() {
        override fun execute(params: TestParams): Flow<Resource<String>> = flow {
            if (shouldThrow) {
                if (customException) {
                    throw CustomException("Custom error")
                }
                throw Exception("Test error")
            }
            emit(Resource.Success(params.value))
        }
    }

    private class NoParamsUseCase : BaseUseCase<Unit, String>() {
        override fun execute(params: Unit): Flow<Resource<String>> = flow {
            emit(Resource.Success("default"))
        }
    }

    private class DelayedUseCase : BaseUseCase<TestParams, String>() {
        override fun execute(params: TestParams): Flow<Resource<String>> = flow {
            kotlinx.coroutines.delay(100) // Simulate some work
            emit(Resource.Success("delayed"))
        }
    }

    private class CustomException(message: String) : Exception(message)
} 