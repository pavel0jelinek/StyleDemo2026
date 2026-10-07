package io.github.pavel0jelinek.demo.eastSouthMaze.maze3

import io.github.pavel0jelinek.demo.eastSouthMaze.Maze

/**
 * Describes a row in a maze of free tiles and obstacles.
 *
 * This interface decouples the computation of the legal paths from a representation of obstacle list.
 */
interface ObstacleRow {
    /**
     * For out of bounds, returns `null`. Returning `true` might simplify the path-computing code,
     * but it would introduce an unhealthy coupling: If we create this interface, we may as well created
     * it well-defined and independent of the problem where we use it.
     */
    fun isObstacle(x: Int): Boolean?

    val sizeX: Int
}

/**
 * An implementation efficient when there are many obstacles - represents a row as a sequence of bits,
 * compressed as Array<ULong>.
 */
class CompressedObstacleRow(override val sizeX: Int, private val compressed: Array<ULong>) : ObstacleRow {

    constructor(obstacles: Iterable<Boolean>) : this(
        obstacles.count(), compress(obstacles)
    )

    override fun isObstacle(x: Int): Boolean? =
        // Implemented by Mistral LLM.
        if (x in 0..<sizeX) (compressed[x ushr 6] and (1UL shl (x and 63))) != 0UL
        else null

    companion object {
        private fun compress(obstacles: Iterable<Boolean>): Array<ULong> {
            val bits = obstacles.toList()
            val words = Array<ULong>((bits.size + 63) / 64) { 0u }
            bits.forEachIndexed { i, b ->
                if (b) words[i ushr 6] = words[i ushr 6] or (1UL shl (i and 63))
            }
            return words
        }
    }
}

/**
 * An implementation memory-efficient when the obstacles are sparse.
 */
class SparseObstacleRow private constructor(
    override val sizeX: Int,
    // Sorted x-indices of all obstacles in this row of maze.
    private val obstacles: List<Int>
) : ObstacleRow {
    private constructor(obstacles: Iterable<Boolean>) : this(
        obstacles.count(), obstacles.withIndex().filter { it.value }.map { it.index }.sorted()
    )

    init {
        obstacles.toList().zipWithNext { a, b -> require(a < b) }
        if (obstacles.isNotEmpty()) {
            require(obstacles.first() >= 0)
            require(obstacles.last() < sizeX)
        }

    }

    override fun isObstacle(x: Int): Boolean? =
        if (x in 0..<sizeX) (0..<sizeX).binaryFindLowestWhich { it >= x } != null
        else null
}


fun ClosedRange<Int>.binaryFindLowestWhich(predicate: (Int) -> Boolean): Int? = if (isEmpty()) null
else {
    val mid = (start + endInclusive) / 2
    if (predicate(mid))
        (start..<mid).binaryFindLowestWhich(predicate) ?: mid
    else ((mid + 1)..endInclusive).binaryFindLowestWhich(predicate)
}

fun Maze.toObstacleRows(sparse:Boolean = false): List<ObstacleRow> = obstacles.map {
    CompressedObstacleRow(it)
}
