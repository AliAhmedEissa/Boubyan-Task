package com.boubyan.articles.data.remote.datasource

import com.boubyan.articles.data.remote.api.NYTimesApi
import com.boubyan.articles.data.remote.dto.ArticleDto
import com.boubyan.articles.data.remote.dto.ArticlesResponseDto
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class ArticlesRemoteDataSourceTest {

    // Mocks
    private val nyTimesApi: NYTimesApi = mock()
    private lateinit var dataSource: ArticlesRemoteDataSource

    // Dispatcher
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dataSource = ArticlesRemoteDataSource(nyTimesApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMostPopularArticles returns successful response`() = runTest {
        // Given: mocked response data
        val dummyArticles = listOf(
            ArticleDto(
                id = 123456,
                url = "https://example.com/article",
                adxKeywords = "test, mock",
                column = null,
                section = "Technology",
                byline = "By John Doe",
                type = "Article",
                title = "Sample Title",
                abstract = "This is a sample abstract.",
                publishedDate = "2025-01-01",
                source = "New York Times",
                views = 100,
                media = ArrayList(),
                etaId = 7890
            )
        )
        val expectedResponse = ArticlesResponseDto(
            status = "OK",
            copyright = "© NYTimes",
            numResults = 1,
            results = dummyArticles
        )
        // Given
        val mockResponse = Response.success(expectedResponse)
        whenever(nyTimesApi.getMostPopularArticles("7")).thenReturn(mockResponse)

        // When
        val result = dataSource.getMostPopularArticles("7")

        // Then
        assertTrue(result.isSuccessful)
        assertEquals(mockResponse, result)
    }

    @Test(expected = RuntimeException::class)
    fun `getMostPopularArticles throws exception on failure`() = runTest {
        // Given
        whenever(nyTimesApi.getMostPopularArticles("7")).thenThrow(RuntimeException("Network error"))

        // When
        dataSource.getMostPopularArticles("7") // should throw RuntimeException
    }
}
