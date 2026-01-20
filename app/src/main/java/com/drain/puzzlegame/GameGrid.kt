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
    val tileSize = with(density) { 64.dp.toPx() }
    val tileSpacing = with(density) { 8.dp.toPx() }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0 until gameState.gridSize) {
                        val position = GridPosition(row, col)
                        val tile = gameState.tiles[position]

                        if (tile != null) {
                            TileView(
                                tile = tile,
                                isInPath = position in gameState.path,
                                number = gameState.getNumberForPosition(position)
                            )
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
    number: Int?,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        tile.isStart -> Color(0xFF4A90E2) // Blue for start
        isInPath -> Color(0xFF7EC8E3) // Light blue for path
        else -> Color(0xFFF5F5F5) // Light gray for empty
    }

    val borderColor = when {
        tile.isStart -> Color(0xFF2E5C8A)
        isInPath -> Color(0xFF4A90E2)
        else -> Color(0xFFCCCCCC)
    }

    Box(
        modifier = modifier
            .size(64.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(3.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number?.toString() ?: if (tile.isStart) "0" else "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (tile.isStart || isInPath) Color.White else Color.Gray
        )
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
