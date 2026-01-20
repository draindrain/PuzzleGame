package com.drain.puzzlegame

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Color scheme for the game tiles
 */
object GameColors {
    // Start tile colors
    val startBackground = Color(0xFF4A90E2)
    val startBorder = Color(0xFF2E5C8A)

    // Path colors
    val pathBackground = Color(0xFF7EC8E3)
    val pathBorder = Color(0xFF4A90E2)

    // Target colors
    val targetUnvisited = Color(0xFFFFB74D)
    val targetUnvisitedBorder = Color(0xFFF57C00)
    val targetMet = Color(0xFF4CAF50)
    val targetMetBorder = Color(0xFF388E3C)
    val targetNotMet = Color(0xFFEF5350)
    val targetNotMetBorder = Color(0xFFC62828)

    // Empty tile colors
    val emptyBackground = Color(0xFFF5F5F5)
    val emptyBorder = Color(0xFFCCCCCC)

    // Modifier colors
    val modifierBackground = Color(0xFF5C6BC0) // Purple-blue
    val modifierBorder = Color(0xFF3949AB)
    val modifierInPath = Color(0xFF7E88C3) // Lighter purple-blue when in path
    val modifierInPathBorder = Color(0xFF5C6BC0)

    // Text colors
    val textActive = Color.White
    val textInactive = Color.Gray

    // UI colors
    val winMessage = Color(0xFF4CAF50)
}

/**
 * Layout constants for the game
 */
object GameDimensions {
    val tileSize = 64.dp
    val tileSpacing = 8.dp
    val tileBorderWidth = 3.dp
    val tileCornerRadius = 12.dp
    val tilePadding = 4.dp

    val gridSpacing = 8.dp
    val sectionSpacing = 16.dp

    val tileFontSize = 24
    val winMessageFontSize = 32
}
