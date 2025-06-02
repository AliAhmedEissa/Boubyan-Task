/*
package com.boubyan.articles.data.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.boubyan.articles.data.remote.api.NYTimesApi
import com.boubyan.articles.data.repositories.ArticlesRepositoryImpl
import com.boubyan.articles.domain.repositories.ArticlesRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DataModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Inject dependencies provided by DataModule
    @Inject
    lateinit var articlesRepository: ArticlesRepository

    @Inject
    lateinit var nyTimesApi: NYTimesApi

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testArticlesRepositoryBinding() {
        // Check that ArticlesRepository is actually ArticlesRepositoryImpl
        assertThat(articlesRepository).isInstanceOf(ArticlesRepositoryImpl::class.java)
    }

    @Test
    fun testNyTimesApiProvision() {
        // Just check the object is not null
        assertThat(nyTimesApi).isNotNull()
    }
}
*/
