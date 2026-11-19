package io.github.pavel0jelinek.styleDemo2026.oneWayMaze

import java.math.BigInteger


/**
 * Now let's do the same as fun [oneWayMaze2][io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze2]
 * using a DSL (domain-specific language). For more complex problems, DSL improves readability.
 */

/** Represents a position in a maze. The coordinates are zero-based. */
data class Position(val x: Int, val y: Int) {
    val westNeighbor get() = Position(x - 1, y)
    val northNeighbor get() = Position(x, y - 1)
}

/**
 * Represents a rectangular maze with free tiles and walls.
 *
 * It could easily implement [io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1996.Maze], but it is not needed for this demonstration
 * of a simple DSL for mazes.
 */
class BooleanMaze(
    // ?? RETHINK NAME
    val walls: List<List<Boolean>>,
) {
    val sizeY = walls.size
    val sizeX = walls.map { it.size }.distinct().singleOrNull()
        ?: throw IllegalArgumentException("The maze must be rectangular")

    val northWestCorner get() = Position(0, 0)
    val southEastCorner get() = Position(sizeX - 1, sizeY - 1)

    fun hasWallAt(position: Position): Boolean? = walls.getOrNull(position.y)?.getOrNull(position.x)

    fun to2DString() = walls.indices.joinToString(separator = "\n") { y ->
        walls[y].indices.joinToString(separator = "") { x ->
            if (walls[y][x]) "X" else "."
        }
    }
}

/**
 * Does the same as [oneWayMaze2][io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze2],
 * but uses a DLS for readability.
 *
 * With default parameters, it solves the problem;
 * with other parameters, it can return the number of paths to any position in the maze.
 */
fun oneWayMaze3(
    maze: BooleanMaze,
    position: Position = maze.southEastCorner,
    cache: MutableMap<Position, BigInteger> = mutableMapOf()
): BigInteger = when {
    // Wall or out-of-bounds: No legal path leads here.
    maze.hasWallAt(position) ?: true -> BigInteger.ZERO
    // Starting point
    position == maze.northWestCorner -> BigInteger.ONE
    // We can arrive from north or from south
    else -> cache.getOrPut(position) {
        oneWayMaze3(maze, position.northNeighbor, cache) +
                oneWayMaze3(maze, position.westNeighbor, cache)
    }
}