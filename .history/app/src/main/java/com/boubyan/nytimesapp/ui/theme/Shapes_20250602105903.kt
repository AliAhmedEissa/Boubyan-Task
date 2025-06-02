package com.boubyan.nytimesapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape definitions for the NY Times App.
 * Defines the corner radius for different size categories of UI elements
 * following Material Design 3 guidelines.
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),  // Used for small chips and tags
    small = RoundedCornerShape(8.dp),       // Used for small cards and buttons
    medium = RoundedCornerShape(12.dp),     // Used for medium-sized cards
    large = RoundedCornerShape(16.dp),      // Used for large cards and dialogs
    extraLarge = RoundedCornerShape(28.dp)  // Used for large containers and sheets
)