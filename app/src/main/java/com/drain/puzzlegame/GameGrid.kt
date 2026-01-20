package com.drain.puzzlegame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameGrid(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val tileSize = with(density) { GameDimensions.tileSize.toPx() }
    val tileSpacing = with(density) { GameDimensions.tileSpacing.toPx() }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(GameDimensions.sectionSpacing)
        ) {
            // Win message
            if (gameState.isComplete) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Puzzle Complete!",
                    fontSize = GameDimensions.winMessageFontSize.sp,
                    fontWeight = FontWeight.Bold,
                    color = GameColors.winMessage
                )
            } else {
                Spacer(modifier = Modifier.height(80.dp))
            }

            // Grid
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(GameDimensions.gridSpacing),
                modifier = Modifier
                    .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()

                            event.changes.forEach { change ->
                                // Get position relative to the grid container
                                val localPos = change.position

                                // Convert to grid position
                                val gridPos = localToGridPosition(
                                    localPos,
                                    tileSize,
                                    tileSpacing,
                                    gameState.gridSize
                                )

                                when {
                                    change.changedToDown() -> {
                                        if (gridPos != null) {
                                            gameState.startDrag(gridPos)
                                            change.consume()
                                        }
                                    }
                                    change.pressed && gridPos != null -> {
                                        gameState.updateDrag(gridPos)
                                        change.consume()
                                    }
                                    change.changedToUp() -> {
                                        gameState.endDrag()
                                        change.consume()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
                for (row in 0 until gameState.gridSize) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(GameDimensions.gridSpacing)
                    ) {
                        for (col in 0 until gameState.gridSize) {
                            val position = GridPosition(row, col)
                            val tile = gameState.tiles[position]

                            if (tile != null) {
                                TileView(
                                    tile = tile,
                                    isInPath = position in gameState.path,
                                    pathNumber = gameState.getNumberForPosition(position),
                                    isTargetMet = gameState.isTargetMet(position)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TileView(
    tile: Tile,
    isInPath: Boolean,
    pathNumber: Int?,
    isTargetMet: Boolean,
    modifier: Modifier = Modifier
) {
    val isTargetInPath = tile.isTarget && isInPath
    val colors = getTileColors(tile, isInPath, isTargetMet, isTargetInPath)

    Box(
        modifier = modifier
            .size(GameDimensions.tileSize)
            .background(colors.background, RoundedCornerShape(GameDimensions.tileCornerRadius))
            .border(GameDimensions.tileBorderWidth, colors.border, RoundedCornerShape(GameDimensions.tileCornerRadius))
            .padding(GameDimensions.tilePadding),
        contentAlignment = Alignment.Center
    ) {
        val displayText = getTileDisplayText(tile, pathNumber)

        Text(
            text = displayText,
            fontSize = GameDimensions.tileFontSize.sp,
            fontWeight = FontWeight.Bold,
            color = colors.text
        )
    }
}

/**
 * Data class to hold tile colors
 */
private data class TileColors(
    val background: Color,
    val border: Color,
    val text: Color
)

/**
 * Get colors for a tile based on its state
 */
private fun getTileColors(
    tile: Tile,
    isInPath: Boolean,
    isTargetMet: Boolean,
    isTargetInPath: Boolean
): TileColors {
    return when {
        tile.isStart -> TileColors(
            background = GameColors.startBackground,
            border = GameColors.startBorder,
            text = GameColors.textActive
        )
        tile.isTarget && isTargetMet -> TileColors(
            background = GameColors.targetMet,
            border = GameColors.targetMetBorder,
            text = GameColors.textActive
        )
        isTargetInPath && !isTargetMet -> TileColors(
            background = GameColors.targetNotMet,
            border = GameColors.targetNotMetBorder,
            text = GameColors.textActive
        )
        tile.isTarget && !isInPath -> TileColors(
            background = GameColors.targetUnvisited,
            border = GameColors.targetUnvisitedBorder,
            text = GameColors.textActive
        )
        tile.isModifier && isInPath -> TileColors(
            background = GameColors.modifierInPath,
            border = GameColors.modifierInPathBorder,
            text = GameColors.textActive
        )
        tile.isModifier -> TileColors(
            background = GameColors.modifierBackground,
            border = GameColors.modifierBorder,
            text = GameColors.textActive
        )
        isInPath -> TileColors(
            background = GameColors.pathBackground,
            border = GameColors.pathBorder,
            text = GameColors.textActive
        )
        else -> TileColors(
            background = GameColors.emptyBackground,
            border = GameColors.emptyBorder,
            text = GameColors.textInactive
        )
    }
}

/**
 * Get the display text for a tile
 */
private fun getTileDisplayText(tile: Tile, pathNumber: Int?): String {
    return when {
        tile.isStart -> "0"
        tile.isTarget -> tile.targetNumber?.toString() ?: ""
        tile.isModifier -> getModifierDisplayText(tile.modifierType, tile.modifierValue)
        pathNumber != null -> pathNumber.toString()
        else -> ""
    }
}

/**
 * Get display text for modifier tiles
 */
private fun getModifierDisplayText(modifierType: ModifierType?, value: Int?): String {
    val valueStr = value?.toString() ?: "0"
    return when (modifierType) {
        ModifierType.RESET -> "=$valueStr"
        ModifierType.ADD -> "+$valueStr"
        ModifierType.SUBTRACT -> "-$valueStr"
        ModifierType.MULTIPLY -> "×$valueStr"
        ModifierType.SET_INCREMENT -> "↑$valueStr"
        null -> ""
    }
}

/**
 * Convert local coordinates (relative to grid container) to grid position
 */
private fun localToGridPosition(
    localPos: Offset,
    tileSize: Float,
    tileSpacing: Float,
    gridSize: Int
): GridPosition? {
    // Calculate which row and column based on position
    // Each tile takes up (tileSize + spacing), except the last one has no trailing spacing
    val row = (localPos.y / (tileSize + tileSpacing)).toInt()
    val col = (localPos.x / (tileSize + tileSpacing)).toInt()

    // Check if within grid bounds
    if (row in 0 until gridSize && col in 0 until gridSize) {
        // Check if we're actually within a tile (not in the spacing)
        val xInTile = localPos.x % (tileSize + tileSpacing)
        val yInTile = localPos.y % (tileSize + tileSpacing)

        if (xInTile <= tileSize && yInTile <= tileSize) {
            return GridPosition(row, col)
        }
    }

    return null
}
