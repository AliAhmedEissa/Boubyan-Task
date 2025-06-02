/*
package com.boubyan.nytimesapp.di

import android.app.Application
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.boubyan.nytimesapp.BuildConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppModuleTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("api_key")
    lateinit var apiKey: String

    @Inject
    lateinit var context: Context

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testApiKeyProvided() {
        // Verify API key from BuildConfig is injected
        assertEquals(BuildConfig.NY_TIMES_API_KEY, apiKey)
    }

    @Test
    fun testApplicationContextProvided() {
        // Check if context is application context
        assertEquals(Application::class.java, context.applicationContext::class.java)
    }
}
*/
