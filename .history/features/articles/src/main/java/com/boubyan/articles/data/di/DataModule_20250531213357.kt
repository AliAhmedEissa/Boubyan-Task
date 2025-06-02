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

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindArticlesRepository(
        articlesRepositoryImpl: ArticlesRepositoryImpl
    ): ArticlesRepository

    companion object {

        @Provides
        @Singleton
        fun provideNYTimesApi(retrofit: Retrofit): NYTimesApi {
            return retrofit.create(NYTimesApi::class.java)
        }
    }
}