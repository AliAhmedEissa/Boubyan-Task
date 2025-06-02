package com.boubyan.articles.presentation.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boubyan.articles.presentation.ui.components.articles.ArticleCard
import com.boubyan.articles.presentation.ui.components.common.ErrorComponent
import com.boubyan.articles.presentation.ui.components.common.LoadingComponent
import com.boubyan.articles.presentation.ui.components.filters.PeriodSelector
import com.boubyan.articles.presentation.ui.intents.ArticlesListIntent
import com.boubyan.articles.presentation.viewmodels.ArticlesListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArticlesListScreen(
    onArticleClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArticlesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    // Pull to refresh state
    val pullToRefreshState = rememberPullToRefreshState()

    // Handle pull to refresh
    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            viewModel.handleIntent(ArticlesListIntent.RefreshArticles)
        }
    }

    // Reset refresh state when loading is complete
    LaunchedEffect(state.isLoading, state.isRefreshing) {
        if (!state.isLoading && !state.isRefreshing) {
            pullToRefreshState.endRefresh()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Search Bar
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { query ->
                    viewModel.handleIntent(ArticlesListIntent.SearchArticles(query))
                },
                onClearQuery = {
                    viewModel.handleIntent(ArticlesListIntent.ClearSearch)
                    keyboardController?.hide()
                },
                modifier = Modifier.padding(16.dp)
            )

            // Period Selector
            AnimatedVisibility(
                visible = !state.isSearching,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                PeriodSelector(
                    periods = state.availablePeriods,
                    onPeriodSelected = { periodValue ->
                        val period = com.boubyan.articles.domain.entities.Period.fromValue(periodValue)
                        viewModel.handleIntent(ArticlesListIntent.SelectPeriod(period))
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content
            when {
                state.isLoading && state.articles.isEmpty() -> {
                    LoadingComponent(
                        message = if (state.isSearching) "Searching articles..." else "Loading articles...",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.hasError && state.articles.isEmpty() -> {
                    ErrorComponent(
                        message = state.error ?: "Unknown error occurred",
                        onRetry = {
                            viewModel.handleIntent(ArticlesListIntent.RetryLoading)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.isEmpty -> {
                    EmptyStateComponent(
                        isSearching = state.isSearching,
                        searchQuery = state.searchQuery,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.showContent -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.articles,
                            key = { it.id }
                        ) { article ->
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article.id) },
                                /*modifier = Modifier.animateItemPlacement(
                                    animationSpec = tween(300)
                                )*/
                            )
                        }

                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        // Pull to refresh indicator
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search articles...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}

@Composable
fun EmptyStateComponent(
    isSearching: Boolean,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSearching) {
                "No articles found for \"$searchQuery\""
            } else {
                "No articles available"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching) {
                "Try searching with different keywords"
            } else {
                "Pull down to refresh"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}