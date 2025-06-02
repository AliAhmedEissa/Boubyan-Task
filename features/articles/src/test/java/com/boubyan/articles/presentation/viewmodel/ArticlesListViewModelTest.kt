/*
package com.boubyan.articles.presentation.viewmodel

import com.boubyan.articles.domain.entities.ArticleFilter
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.usecases.GetMostPopularArticlesUseCase
import com.boubyan.articles.domain.usecases.SearchArticlesUseCase
import com.boubyan.articles.presentation.mappers.ArticleUiMapper
import com.boubyan.articles.presentation.ui.intents.ArticlesListIntent
import com.boubyan.articles.presentation.viewmodels.ArticlesListViewModel
import com.boubyan.core.base.Resource
import com.boubyan.core.error.ErrorMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesListViewModelTest {

    private lateinit var viewModel: ArticlesListViewModel

    private val getMostPopularArticlesUseCase = mockk<GetMostPopularArticlesUseCase>()
    private val searchArticlesUseCase = mockk<SearchArticlesUseCase>()
    private val articleUiMapper = mockk<ArticleUiMapper>()
    private val errorMapper = mockk<ErrorMapper>()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = ArticlesListViewModel(
            getMostPopularArticlesUseCase,
            searchArticlesUseCase,
            articleUiMapper,
            errorMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadArticles with no searchQuery should fetch articles successfully`() = testScope.runTest {
        // Arrange
        val mockArticles = listOf(*/
/* your domain model here *//*
)
        val mappedUiArticles = listOf(*/
/* your UI model here *//*
)

        val filter = ArticleFilter(period = Period.getDefault())
        val params = GetMostPopularArticlesUseCase.Params(filter)

        coEvery { getMostPopularArticlesUseCase.invoke(params) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockArticles)
        )
        every { articleUiMapper.mapToUiModelList(mockArticles) } returns mappedUiArticles

        // Act
        viewModel.handleIntent(ArticlesListIntent.LoadArticles)
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(mappedUiArticles, state.articles)
        assertNull(state.error)
    }

    @Test
    fun `searchArticles with short query should clear results`() = testScope.runTest {
        // Act
        viewModel.handleIntent(ArticlesListIntent.SearchArticles("a"))
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state.articles.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `searchArticles with valid query returns results`() = testScope.runTest {
        val query = "tech"
        val domainResults = listOf(*/
/* domain models *//*
)
        val uiResults = listOf(*/
/* UI models *//*
)

        coEvery { searchArticlesUseCase(any()) } returns flowOf(
            Resource.Loading,
            Resource.Success(domainResults)
        )
        every { articleUiMapper.mapToUiModelList(domainResults) } returns uiResults

        // Act
        viewModel.handleIntent(ArticlesListIntent.SearchArticles(query))
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(uiResults, state.articles)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchArticles returns error and maps message`() = testScope.runTest {
        val filter = ArticleFilter(period = Period.getDefault())
        val exception = RuntimeException("Network error")
        val userMessage = "Something went wrong"

        coEvery {
            getMostPopularArticlesUseCase(GetMostPopularArticlesUseCase.Params(filter))
        } returns flowOf(Resource.Error(exception))

        every { errorMapper.getUserFriendlyMessage(exception) } returns userMessage

        // Act
        viewModel.handleIntent(ArticlesListIntent.LoadArticles)
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertEquals(userMessage, state.error)
        assertFalse(state.isLoading)
    }
}*/
