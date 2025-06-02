package com.boubyan.nytimesapp.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.boubyan.articles.presentation.navigation.Articles
import com.boubyan.articles.presentation.navigation.articlesNavigation
import com.boubyan.nytimesapp.ui.components.AppTopBar

/**
 * Main navigation composable for the NY Times App.
 * Sets up the navigation structure and handles navigation between different screens.
 * Uses Jetpack Navigation Compose for navigation management.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Articles,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // Articles feature navigation
            articlesNavigation(navController)
        }
    }
}