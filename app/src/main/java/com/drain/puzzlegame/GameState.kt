package com.drain.puzzlegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Represents a position in the grid
 */
data class GridPosition(val row: Int, val col: Int)

/**
 * Represents a tile in the grid
 */
data class Tile(
    val position: GridPosition,
    val isStart: Boolean = false,
    val number: Int? = null
)

/**
 * Game state management
 */
class GameState(val gridSize: Int = 5) {
    var tiles by mutableStateOf<Map<GridPosition, Tile>>(emptyMap())
        private set

    var path by mutableStateOf<List<GridPosition>>(emptyList())
        private set

    var isDragging by mutableStateOf(false)
        private set

    init {
        initializeGrid()
    }

    private fun initializeGrid() {
        // Create a 5x5 grid with start position at (2, 2) - center
        val startPos = GridPosition(gridSize / 2, gridSize / 2)
        tiles = buildMap {
            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    val pos = GridPosition(row, col)
                    put(pos, Tile(
                        position = pos,
                        isStart = pos == startPos
                    ))
                }
            }
        }
    }

    fun startDrag(position: GridPosition) {
        val tile = tiles[position]

        // Can start from the start tile (0) or continue from the last position in path
        when {
            tile?.isStart == true -> {
                // Starting from 0 - begin new path
                isDragging = true
                path = listOf(position)
            }
            path.isNotEmpty() && position == path.last() -> {
                // Continuing from the last position
                isDragging = true
            }
        }
    }

    fun updateDrag(position: GridPosition) {
        if (!isDragging) return

        // Check if position is valid (within grid)
        if (position.row !in 0 until gridSize || position.col !in 0 until gridSize) return

        // Check if this is the previous position in path (going backwards)
        if (path.size > 1 && position == path[path.size - 2]) {
            // Remove last position (undo)
            path = path.dropLast(1)
            return
        }

        // Check if already in path
        if (position in path) return

        // Check if adjacent to last position (not diagonal)
        val lastPos = path.lastOrNull() ?: return
        val rowDiff = kotlin.math.abs(position.row - lastPos.row)
        val colDiff = kotlin.math.abs(position.col - lastPos.col)

        if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
            // Valid adjacent move
            path = path + position
        }
    }

    fun endDrag() {
        isDragging = false
    }

    fun resetPath() {
        path = emptyList()
        isDragging = false
    }

    fun getNumberForPosition(position: GridPosition): Int? {
        val index = path.indexOf(position)
        return if (index >= 0) index else null
    }
}
