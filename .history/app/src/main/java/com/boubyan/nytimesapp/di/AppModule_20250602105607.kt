package com.boubyan.nytimesapp.di

import android.content.Context
import com.boubyan.nytimesapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides application-wide dependencies.
 * This module is installed in the SingletonComponent to ensure dependencies
 * are scoped to the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * Provides the NY Times API key from the BuildConfig.
     * This is a singleton provider to ensure the API key is available throughout the app.
     *
     * @return The NY Times API key as a String
     */
    @Provides
    @Singleton
    @Named("api_key")
    fun provideApiKey(): String {
        return BuildConfig.NY_TIMES_API_KEY
    }
    
    /**
     * Provides the application context.
     * This is a singleton provider to ensure the context is available throughout the app.
     *
     * @param context The application context provided by Hilt
     * @return The application context
     */
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}