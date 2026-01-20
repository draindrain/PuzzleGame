package com.drain.puzzlegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Game state management
 */
class GameState(private val level: LevelConfig) {
    val gridSize: Int = level.gridSize

    var tiles by mutableStateOf<Map<GridPosition, Tile>>(emptyMap())
        private set

    var path by mutableStateOf<List<GridPosition>>(emptyList())
        private set

    var isDragging by mutableStateOf(false)
        private set

    var isComplete by mutableStateOf(false)
        private set

    init {
        initializeGrid()
    }

    private fun initializeGrid() {
        tiles = buildMap {
            // Create all tiles as empty first
            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    val pos = GridPosition(row, col)
                    put(pos, Tile(position = pos, type = TileType.EMPTY))
                }
            }

            // Set start tile
            put(level.startPosition, Tile(
                position = level.startPosition,
                type = TileType.START
            ))

            // Set target tiles
            level.targets.forEach { target ->
                put(target.position, Tile(
                    position = target.position,
                    type = TileType.TARGET,
                    targetNumber = target.requiredNumber
                ))
            }

            // Set modifier tiles (for Phase 4+)
            level.modifiers.forEach { modifier ->
                put(modifier.position, Tile(
                    position = modifier.position,
                    type = TileType.MODIFIER,
                    modifierType = modifier.type,
                    modifierValue = modifier.value
                ))
            }
        }
    }

    companion object {
        /**
         * Create a GameState with a default test level
         */
        fun createDefaultLevel(): GameState {
            val level = LevelConfig.createSimpleLevel(
                gridSize = 5,
                targets = listOf(
                    TargetConfig(GridPosition(1, 3), 6)
                )
            )
            return GameState(level)
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
        if (!position.isWithinBounds(gridSize)) return

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
        if (position.isAdjacentTo(lastPos)) {
            // Valid adjacent move
            path = path + position
            checkWinCondition()
        }
    }

    fun endDrag() {
        isDragging = false
    }

    fun resetPath() {
        path = emptyList()
        isDragging = false
        isComplete = false
    }

    fun getNumberForPosition(position: GridPosition): Int? {
        val index = path.indexOf(position)
        return if (index >= 0) index else null
    }

    private fun checkWinCondition() {
        // Get all target tiles
        val targetTiles = tiles.values.filter { it.isTarget }

        // Check if all targets are satisfied
        val allTargetsMet = targetTiles.all { isTargetMet(it.position) }

        isComplete = allTargetsMet && targetTiles.isNotEmpty()
    }

    fun isTargetMet(position: GridPosition): Boolean {
        val tile = tiles[position] ?: return false
        if (!tile.isTarget || tile.targetNumber == null) return false

        val pathIndex = path.indexOf(position)
        return pathIndex >= 0 && pathIndex == tile.targetNumber
    }

    /**
     * Get all target tiles
     */
    fun getTargets(): List<Tile> = tiles.values.filter { it.isTarget }

    /**
     * Get all modifier tiles
     */
    fun getModifiers(): List<Tile> = tiles.values.filter { it.isModifier }
}
