package com.boubyan.articles.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.boubyan.articles.presentation.ui.screens.ArticleDetailScreen
import com.boubyan.articles.presentation.ui.screens.ArticlesListScreen
import kotlinx.serialization.Serializable

// Define type-safe routes using @Serializable
@Serializable
object Articles

@Serializable
data class ArticleDetail(val articleId: Long)

// Extension functions for type-safe navigation
fun NavController.navigateToArticles() {
    navigate(Articles) {
        launchSingleTop = true
    }
}

fun NavController.navigateToArticleDetail(articleId: Long) {
    navigate(ArticleDetail(articleId))
}

// Type-safe navigation graph builder
fun NavGraphBuilder.articlesNavigation(
    navController: NavController
) {
    composable<Articles>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        ArticlesListScreen(
            onArticleClick = { articleId ->
                navController.navigateToArticleDetail(articleId)
            }
        )
    }

    composable<ArticleDetail>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) { backStackEntry ->
        // Extract the type-safe arguments
        val articleDetail = backStackEntry.toRoute<ArticleDetail>()

        ArticleDetailScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}