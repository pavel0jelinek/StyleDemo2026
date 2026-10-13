package io.github.pavel0jelinek.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.demo.oneWayMaze.ObstacleMaze
import io.github.pavel0jelinek.demo.oneWayMaze.Position

/**
 * TTTTTT
 *
 * This interface decouples the computation of the legal paths from a representation of obstacle list.
 */
interface Maze {
    val sizeX: Int
    val sizeY:Int
    /**
     * For out of bounds, returns `null`. Returning `true` might simplify the path-computing code,
     * but it would introduce an unhealthy coupling: If we create this interface, we may as well created
     * it well-defined and independent of the problem where we use it.
     */
    fun isObstacle(position: Position): Boolean?


}

class CompressedBooleans(val size: Int, private val compressed: Array<ULong>) {

    constructor(bools: Iterable<Boolean>) : this(
        bools.count(),
        compress(bools)
    )

    operator fun get(index: Int): Boolean? =
        if (index in 0 until size) (compressed[index ushr 6] and (1UL shl (index and 63))) != 0UL
        else null

    fun toBools(): List<Boolean> =
        List(size) { i ->
            (compressed[i ushr 6] and (1UL shl (i and 63))) != 0UL
        }

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

class BooleanMaze(
    private val rows: List<CompressedBooleans>
) : Maze {
    override val sizeY: Int get() = rows.size
    override val sizeX: Int get() = rows.firstOrNull()?.size ?: 0

    override fun isObstacle(position: Position): Boolean? {
        if (position.y !in 0 until sizeY) return null
        return rows[position.y][position.x]
    }

    companion object {
        fun of(obstacles: Iterable<Iterable<Boolean>>): BooleanMaze {
            val compressedRows = obstacles.map { CompressedBooleans(it) }
            val sizeX = compressedRows.map { it.size }.distinct().singleOrNull()
                ?: throw NotImplementedError("Rectangular maze expected.")
            return BooleanMaze(compressedRows)
        }

        fun ofPredicate(sizeX: Int, sizeY: Int, isObstacle: Position.() -> Boolean): BooleanMaze {
            val compressedRows = (0 until sizeY).map { y ->
                CompressedBooleans((0 until sizeX).map { x ->
                    Position(x, y).isObstacle()
                })
            }
            return BooleanMaze(compressedRows)
        }
    }
}

/**
 * An implementation memory-efficient when the obstacles are sparse.
 */
class SparseObstacleRow private constructor(
    // Every list item contains one row.
    // Every row is an ascending list of x-coordinates of obstacles in this maze row.
    private val rows: List<List<Int>>,
    override val sizeX: Int
) : Maze {
    override val sizeY: Int get() = rows.size

    constructor(obstacles: Iterable<Iterable<Boolean>>) : this(
        obstacles.map { row ->
            row.withIndex().filter { it.value }.map { it.index }
        },
        obstacles.firstOrNull()?.count() ?: 0
    )

    init {
        for (row in rows) {
            row.zipWithNext { a, b -> require(a < b) }
            if (row.isNotEmpty()) {
                require(row.first() >= 0)
                require(row.last() < sizeX)
            }
        }
    }

    override fun isObstacle(position: Position): Boolean? {
        if (position.y !in 0 until sizeY) return null
        if (position.x !in 0 until sizeX) return null
        val row = rows[position.y]
        return row.binarySearch(position.x) >= 0
    }
}

fun ClosedRange<Int>.binaryFindLowestWhich(predicate: (Int) -> Boolean): Int? = if (isEmpty()) null
else {
    val mid = (start + endInclusive) / 2
    if (predicate(mid))
        (start..<mid).binaryFindLowestWhich(predicate) ?: mid
    else ((mid + 1)..endInclusive).binaryFindLowestWhich(predicate)
}
