package com.drain.puzzlegame

import kotlin.math.abs

/**
 * Represents a position in the grid
 */
data class GridPosition(val row: Int, val col: Int) {
    /**
     * Check if this position is adjacent (not diagonal) to another position
     */
    fun isAdjacentTo(other: GridPosition): Boolean {
        val rowDiff = abs(row - other.row)
        val colDiff = abs(col - other.col)
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)
    }

    /**
     * Check if position is within grid bounds
     */
    fun isWithinBounds(gridSize: Int): Boolean {
        return row in 0 until gridSize && col in 0 until gridSize
    }
}

/**
 * Type of special tile
 */
enum class TileType {
    EMPTY,      // Regular empty tile
    START,      // Starting position (0)
    TARGET,     // Target tile with a number requirement
    MODIFIER    // Modifier tile (for Phase 4)
}

/**
 * Modifier types for Phase 4+
 */
enum class ModifierType {
    RESET,      // Reset counter to 0
    ADD,        // Add to counter
    SUBTRACT,   // Subtract from counter
    MULTIPLY,   // Multiply counter
    SET_INCREMENT // Change increment value (+1 becomes +2, etc)
}

/**
 * Represents a tile in the grid
 */
data class Tile(
    val position: GridPosition,
    val type: TileType = TileType.EMPTY,
    val targetNumber: Int? = null,          // For TARGET tiles
    val modifierType: ModifierType? = null, // For MODIFIER tiles (Phase 4)
    val modifierValue: Int? = null          // Value for modifier (Phase 4)
) {
    val isStart: Boolean get() = type == TileType.START
    val isTarget: Boolean get() = type == TileType.TARGET
    val isModifier: Boolean get() = type == TileType.MODIFIER
}

/**
 * Level configuration - makes it easy to define new levels
 */
data class LevelConfig(
    val gridSize: Int,
    val startPosition: GridPosition,
    val targets: List<TargetConfig>,
    val modifiers: List<ModifierConfig> = emptyList() // For Phase 4+
) {
    companion object {
        /**
         * Create a simple level with start in center
         */
        fun createSimpleLevel(
            gridSize: Int = 5,
            targets: List<TargetConfig>
        ): LevelConfig {
            return LevelConfig(
                gridSize = gridSize,
                startPosition = GridPosition(gridSize / 2, gridSize / 2),
                targets = targets
            )
        }
    }
}

/**
 * Target tile configuration
 */
data class TargetConfig(
    val position: GridPosition,
    val requiredNumber: Int
)

/**
 * Modifier tile configuration (for Phase 4+)
 */
data class ModifierConfig(
    val position: GridPosition,
    val type: ModifierType,
    val value: Int = 0
)
