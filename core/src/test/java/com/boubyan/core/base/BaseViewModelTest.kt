package com.boubyan.core.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // @get:Rule
    // val coroutineTestRule = CoroutineTestRule()

    private lateinit var testViewModel: TestViewModel

    @Before
    fun setup() {
        testViewModel = TestViewModel()
    }

    @Test
    fun `initial state should be set correctly`() = runTest {
        testViewModel.state.test {
            val initialState = awaitItem()
            assertEquals(TestState(), initialState)
        }
    }

    @Test
    fun `setState should update state correctly`() = runTest {
        val newState = TestState(isLoading = true, message = "Loading")

        testViewModel.updateStatePublic(newState)

        testViewModel.state.test {
            val updatedState = awaitItem()
            assertEquals(newState, updatedState)
        }
    }

    @Test
    fun `updateState should modify state correctly`() = runTest {
        testViewModel.updateStatePublic { it.copy(isLoading = true) }

        testViewModel.state.test {
            val updatedState = awaitItem()
            assertTrue(updatedState.isLoading)
        }
    }

    @Test
    fun `handleIntent should process intents correctly`() = runTest {
        testViewModel.handleIntent(TestIntent.StartLoading)

        testViewModel.state.test {
            val state = awaitItem()
            assertTrue(state.isLoading)
            assertEquals("Started loading", state.message)
        }
    }

    // Test implementation classes
    data class TestState(
        val isLoading: Boolean = false,
        val message: String = ""
    )

    sealed class TestIntent {
        object StartLoading : TestIntent()
        object StopLoading : TestIntent()
        data class SetMessage(val message: String) : TestIntent()
    }

    class TestViewModel : BaseViewModel<TestState, TestIntent>() {
        override fun getInitialState(): TestState = TestState()

        override fun handleIntent(intent: TestIntent) {
            when (intent) {
                is TestIntent.StartLoading -> {
                    updateState { it.copy(isLoading = true, message = "Started loading") }
                }
                is TestIntent.StopLoading -> {
                    updateState { it.copy(isLoading = false, message = "Stopped loading") }
                }
                is TestIntent.SetMessage -> {
                    updateState { it.copy(message = intent.message) }
                }
            }
        }

        // Public method for testing
        fun updateStatePublic(newState: TestState) = setState(newState)
        fun updateStatePublic(update: (TestState) -> TestState) = updateState(update)
    }
}