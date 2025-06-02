package com.boubyan.core.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `test successful execution`() = runTest {
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
    fun `test error execution`() = runTest {
        // Given
        val useCase = TestUseCase(shouldThrow = true)
        val params = TestParams("test")

        // When
        val result = useCase(params).first()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Test error", (result as Resource.Error).exception.message)
    }

    private data class TestParams(val value: String)

    private class TestUseCase(
        private val shouldThrow: Boolean = false
    ) : BaseUseCase<TestParams, String>() {
        override suspend fun execute(params: TestParams): String {
            if (shouldThrow) {
                throw Exception("Test error")
            }
            return params.value
        }
    }
} 