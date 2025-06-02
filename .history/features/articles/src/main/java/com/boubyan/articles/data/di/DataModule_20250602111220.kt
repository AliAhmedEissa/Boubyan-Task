package com.boubyan.articles.data.di

import com.boubyan.articles.data.remote.api.NYTimesApi
import com.boubyan.articles.data.repositories.ArticlesRepositoryImpl
import com.boubyan.articles.domain.repositories.ArticlesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides data layer dependencies for the articles feature.
 * All dependencies are scoped to the application lifecycle using SingletonComponent.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    /**
     * Binds the ArticlesRepository implementation to its interface.
     * This allows for dependency injection of the repository throughout the app.
     *
     * @param articlesRepositoryImpl The concrete implementation of ArticlesRepository
     * @return The ArticlesRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindArticlesRepository(
        articlesRepositoryImpl: ArticlesRepositoryImpl
    ): ArticlesRepository

    companion object {
        /**
         * Provides an instance of NYTimesApi using Retrofit.
         * Creates the API interface implementation for making network calls.
         *
         * @param retrofit The Retrofit instance to use for creating the API
         * @return An implementation of NYTimesApi
         */
        @Provides
        @Singleton
        fun provideNYTimesApi(retrofit: Retrofit): NYTimesApi {
            return retrofit.create(NYTimesApi::class.java)
        }
    }
}