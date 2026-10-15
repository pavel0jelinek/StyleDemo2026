package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze

import java.math.BigInteger

/**
 * Function [oneWayMaze1] was very slow. In a modern programming language,
 * it can be sped up very simply by wrapping the computation inside a cache.
 *
 * This decreases the time complexity on an MxN maze from `O(2^min(M,N))` to `O(M*N)`.
 *
 * Also, let's use [BigInteger] because the range of [Long] can easily be exceeded.
 */
fun oneWayMaze2a(
    walls: List<List<Boolean>>,
    x: Int = walls[0].lastIndex,
    y: Int = walls.lastIndex,
    cache: MutableMap<Pair<Int, Int>, BigInteger> = mutableMapOf()
): BigInteger = when {
    // An wall or out-of-bounds.
    walls.getOrNull(y)?.getOrNull(x) ?: true -> BigInteger.ZERO
    // Starting point
    x == 0 && y == 0 -> BigInteger.ONE
    else -> cache.getOrPut(x to y) {
        // We can arrive from the west or from the north
        oneWayMaze2a(walls, x - 1, y, cache) +
                oneWayMaze2a(walls, x, y - 1, cache)
    }
}

/**
 * Now let's do the same using a DSL (domain-specific language). For more complext problems, DSL improves readability.
 */

/** Represents a position in a maze. The coordinates are zero-based. */
data class Position(val x: Int, val y: Int) {
    val westNeighbor get() = Position(x - 1, y)
    val northNeighbor get() = Position(x, y - 1)
}

/**
 * Represents a rectangular maze with free tiles and walls.
 */
class BooleanMaze( // ?? RETHINK NAME
    val walls: List<List<Boolean>>,
) {
    val sizeY = walls.size
    val sizeX = walls.map { it.size }.distinct().singleOrNull()
        ?: throw IllegalArgumentException("The maze must be rectangular")

    val northWestCorner get() = Position(0, 0)
    val southEastCorner get() = Position(sizeX - 1, sizeY - 1)


    fun hasWallAt(position: Position): Boolean? = walls.getOrNull(position.y)?.getOrNull(position.x)

    fun to2DString() = walls.indices.joinToString(separator = "\n") { y ->
        walls[y].indices.joinToString (separator = "") { x ->
            if (walls[y][x]) "X" else "."
        }
    }
}

/**
 * Again, with default parameters, it solves the problem;
 * with other parameters, it can return the number of paths to any position in the maze.
 */
fun oneWayMaze2b(
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
        oneWayMaze2b(maze, position.northNeighbor, cache) +
                oneWayMaze2b(maze, position.westNeighbor, cache)
    }
}


fun main() {

}