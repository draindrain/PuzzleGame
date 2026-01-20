# Overview

This is a puzzle game where the player drags his finger over a grid of squares from a starting point (0) until he has hit a number of target tiles completing the constraint for each of them.

## Movement

The player starts by holding down their finger on the 0-tile. They can then drag to an adjacent tile (not diagonally), for each step a counter increases by 1 and the corresponding number is written on that tile, starting from the 0 tile. The player can cancel moved by moving to the previous tile. Or resetting the whole sequence by lifting their finger and pressing the 0 tile again. You are allowed to lift your finger and set it down on the active tile to continue.

## Special tiles

There are two types of special tiles in the game, target tiles and modifier tiles. Target tiles must all be hit by the player and contain some constraint, usually a number that must be the same as the number written on the tile by thing counter as the player moves onto that tile. Target tiles start out as red and turn green once cleared.

There are also modifier tiles, they somehow modify the counter. Setting it to 0, adding or subtracting from it or changing how it updated on each step (-1 or +3 instead of +1 for example)

## Screen layout.

The screen consists of the X by X grid, starting at 5x5.

## Generating levels

New levels can be randomized by doing a random walk in the grid, then adding in some target tiles and modifier tiles along the way. Randomise the modifier tiles and just note the numbers that end up on the target tiles. All of the modifier tiles dont have to be in the critical tiles, but a majority of them should be.

## Increasing difficulty

Difficulty can be increased every X number of levels by increasing the size of the grid, and adding more special tiles or adding new types of special tiles (more complex)

Starting grid is 5x5 and contains two target tiles and one modifier tile.

# Development plan

## Phase 1

Create a 5x5 grid (but make sure the size is variable for future phases). Let one of the squares be 0 and make sure the user is able to hold and drag a path through the grid as well as cancel the path by dragging backwards to previous grids.

# Phase 2

Add a target square with a number on it. When the player drags a path from 0, add incrementing numbers to each square passed. If the number corresponds with the one on the target square, complete the puzzle.

# Phase 3

Add colors. The Target squares should start out as yellow and turn red or green when passed depending on if the constraint is fulfilled or not. The 0 and the path built by the player should be colored light blue.

# Phase 4

Add a modifier square to the board. Make it a simple one that just resets the counter to 0. The modifier should be blue.

# Phase 5

Write the code that given a size and a number of modifiers and targets, generates a puzzle being doing a random walk like described above.

Use that code to generate a new level with a 5x5 grid, 2 targets and 1 modifier when the game is loaded and each time a board is cleared.
