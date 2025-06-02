package com.boubyan.articles.domain

import app.cash.turbine.test
import com.boubyan.articles.domain.entities.Article
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.boubyan.articles.domain.usecases.GetArticleDetailsUseCase
import com.boubyan.core.base.Resource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetArticleDetailsUseCaseTest {

    private val repository: ArticlesRepository = mockk()
    private lateinit var useCase: GetArticleDetailsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetArticleDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns Error when articleId is invalid`() = runTest {
        val params = GetArticleDetailsUseCase.Params(articleId = 0)

        val flow = useCase(params)

        flow.test {
            val emission = awaitItem()
            assertTrue(emission is Resource.Error)
            assertEquals("Invalid article ID", (emission as Resource.Error).exception.message)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `invoke returns Error when repository returns invalid article`() = runTest {
        val article = Article.empty()
        val params = GetArticleDetailsUseCase.Params(articleId = 1)

        coEvery { repository.getArticleById(1) } returns flowOf(Resource.Success(article))

        val flow = useCase(params)

        flow.test {
            val emission = awaitItem()
            assertTrue(emission is Resource.Error)
            assertEquals("Invalid article data", (emission as Resource.Error).exception.message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

