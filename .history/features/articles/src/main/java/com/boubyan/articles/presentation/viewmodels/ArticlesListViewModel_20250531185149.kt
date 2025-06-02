package com.boubyan.articles.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.boubyan.articles.domain.entities.ArticleFilter
import com.boubyan.articles.domain.entities.Period
import com.boubyan.articles.domain.usecases.GetMostPopularArticlesUseCase
import com.boubyan.articles.domain.usecases.SearchArticlesUseCase
import com.boubyan.articles.presentation.mappers.ArticleUiMapper
import com.boubyan.articles.presentation.models.PeriodUiModel
import com.boubyan.articles.presentation.ui.intents.ArticlesListIntent
import com.boubyan.articles.presentation.ui.states.ArticlesListState
import com.boubyan.core.base.BaseViewModel
import com.boubyan.core.base.Resource
import com.boubyan.core.error.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ArticlesListViewModel @Inject constructor(
    private val getMostPopularArticlesUseCase: GetMostPopularArticlesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val articleUiMapper: ArticleUiMapper,
    private val errorMapper: ErrorMapper
) : BaseViewModel<ArticlesListState, ArticlesListIntent>() {
    
    private val TAG = "ArticlesListViewModel"
    
    override fun getInitialState(): ArticlesListState {
        Log.d(TAG, "Initializing ArticlesListViewModel")
        return ArticlesListState(
            availablePeriods = PeriodUiModel.from(
                Period.getAllPeriods(),
                Period.getDefault().value
            )
        )
    }
    
    init {
        Log.d(TAG, "ArticlesListViewModel created, loading initial articles")
        loadArticles()
    }
    
    override fun handleIntent(intent: ArticlesListIntent) {
        Log.d(TAG, "Handling intent: $intent")
        when (intent) {
            is ArticlesListIntent.LoadArticles -> loadArticles()
            is ArticlesListIntent.RefreshArticles -> refreshArticles()
            is ArticlesListIntent.SelectPeriod -> selectPeriod(intent.period)
            is ArticlesListIntent.SearchArticles -> searchArticles(intent.query)
            is ArticlesListIntent.ClearSearch -> clearSearch()
            is ArticlesListIntent.RetryLoading -> retryLoading()
        }
    }
    
    private fun loadArticles() {
        Log.d(TAG, "Loading articles")
        if (state.value.searchQuery.isNotEmpty()) {
            Log.d(TAG, "Search query present, performing search")
            performSearch(state.value.searchQuery)
        } else {
            Log.d(TAG, "No search query, fetching articles")
            fetchArticles()
        }
    }
    
    private fun fetchArticles() {
        Log.d(TAG, "Fetching articles for period: ${state.value.selectedPeriod}")
        val filter = ArticleFilter(period = state.value.selectedPeriod)
        
        getMostPopularArticlesUseCase(GetMostPopularArticlesUseCase.Params(filter))
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d(TAG, "Loading articles...")
                        updateState { 
                            it.copy(
                                isLoading = true,
                                error = null,
                                isRefreshing = false
                            )
                        }
                    }
                    is Resource.Success -> {
                        Log.d(TAG, "Articles loaded successfully: ${resource.data.size} articles")
                        val uiArticles = articleUiMapper.mapToUiModelList(resource.data)
                        updateState {
                            it.copy(
                                isLoading = false,
                                articles = uiArticles,
                                error = null,
                                isRefreshing = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "Error loading articles", resource.exception)
                        val errorMessage = errorMapper.getUserFriendlyMessage(resource.exception)
                        updateState {
                            it.copy(
                                isLoading = false,
                                error = errorMessage,
                                isRefreshing = false
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun refreshArticles() {
        updateState { it.copy(isRefreshing = true) }
        loadArticles()
    }
    
    private fun selectPeriod(period: Period) {
        updateState {
            it.copy(
                selectedPeriod = period,
                availablePeriods = PeriodUiModel.from(
                    Period.getAllPeriods(),
                    period.value
                )
            )
        }
        loadArticles()
    }
    
    private fun searchArticles(query: String) {
        updateState { 
            it.copy(
                searchQuery = query,
                isSearching = query.isNotEmpty()
            )
        }
        
        if (query.isEmpty()) {
            loadArticles()
        } else {
            performSearch(query)
        }
    }
    
    private fun performSearch(query: String) {
        if (query.length < 2) {
            updateState {
                it.copy(
                    articles = emptyList(),
                    isLoading = false,
                    error = null
                )
            }
            return
        }
        
        val params = SearchArticlesUseCase.Params(
            query = query,
            period = state.value.selectedPeriod
        )
        
        searchArticlesUseCase(params)
            .debounce(300)
            .distinctUntilChanged()
            .onEach { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        updateState { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val uiArticles = articleUiMapper.mapToUiModelList(resource.data)
                        updateState {
                            it.copy(
                                isLoading = false,
                                articles = uiArticles,
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
    
    private fun clearSearch() {
        updateState {
            it.copy(
                searchQuery = "",
                isSearching = false
            )
        }
        loadArticles()
    }
    
    private fun retryLoading() {
        updateState { it.copy(error = null) }
        loadArticles()
    }
}