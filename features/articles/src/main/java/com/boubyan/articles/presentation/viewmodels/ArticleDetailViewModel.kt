package com.boubyan.articles.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.boubyan.articles.domain.usecases.GetArticleDetailsUseCase
import com.boubyan.articles.presentation.mappers.ArticleUiMapper
import com.boubyan.articles.presentation.ui.states.ArticleDetailState
import com.boubyan.articles.presentation.ui.intents.ArticleDetailIntent
import com.boubyan.core.base.BaseViewModel
import com.boubyan.core.base.Resource
import com.boubyan.core.error.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val getArticleDetailsUseCase: GetArticleDetailsUseCase,
    private val articleUiMapper: ArticleUiMapper,
    private val errorMapper: ErrorMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ArticleDetailState, ArticleDetailIntent>() {
    
    private val articleId: Long = savedStateHandle.get<Long>("articleId") ?: 0L
    
    override fun getInitialState(): ArticleDetailState {
        return ArticleDetailState()
    }
    
    init {
        if (articleId != 0L) {
            loadArticle(articleId)
        }
    }
    
    override fun handleIntent(intent: ArticleDetailIntent) {
        when (intent) {
            is ArticleDetailIntent.LoadArticle -> loadArticle(intent.articleId)
            is ArticleDetailIntent.RefreshArticle -> refreshArticle()
            is ArticleDetailIntent.ToggleBookmark -> toggleBookmark()
            is ArticleDetailIntent.ShareArticle -> shareArticle()
            is ArticleDetailIntent.OpenInBrowser -> openInBrowser()
            is ArticleDetailIntent.RetryLoading -> retryLoading()
        }
    }
    
    private fun loadArticle(articleId: Long) {
        val params = GetArticleDetailsUseCase.Params(articleId)
        
        getArticleDetailsUseCase(params)
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        updateState {
                            it.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                    }
                    is Resource.Success -> {
                        val uiArticle = articleUiMapper.mapToUiModel(resource.data)
                        updateState {
                            it.copy(
                                isLoading = false,
                                article = uiArticle,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        val errorMessage = errorMapper.getUserFriendlyMessage(resource.exception)
                        updateState {
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun refreshArticle() {
        if (articleId != 0L) {
            loadArticle(articleId)
        }
    }
    
    private fun toggleBookmark() {
        updateState {
            it.copy(isBookmarked = !it.isBookmarked)
        }
        // TODO: Implement bookmark functionality
    }
    
    private fun shareArticle() {
        // TODO: Implement share functionality
    }
    
    private fun openInBrowser() {
        // TODO: Implement open in browser functionality
    }
    
    private fun retryLoading() {
        updateState { it.copy(error = null) }
        if (articleId != 0L) {
            loadArticle(articleId)
        }
    }
}