package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.Position

/**
 * TTTTTT
 *
 * This interface decouples the computation of the legal paths from a representation of wall list.
 */
interface Maze {
    // sizeX and sizeY are always positive.
    val sizeX: Int
    val sizeY:Int

    val indicesX get()= 0..<sizeX
    val indicesY get() = 0..sizeY
    /**
     * TTT
     *
     * For out of bounds, returns `null`. Returning `true` might simplify the path-computing code,
     * but it would introduce an unhealthy coupling: If we create this interface, we may as well created
     * it well-defined and independent of the problem where we use it.
     */
    fun isWall(x:Int, y:Int): Boolean?
}


class CompressedMaze(
    private val rows: List<CompressedBooleans>
) : Maze {
    override val sizeY: Int get() = rows.size
    override val sizeX: Int get() = rows.firstOrNull()?.size ?: 0

    override fun isWall(x: Int, y: Int): Boolean? =
        if (x!in indicesX || y!in indicesY) null
    else rows[y][x]

    companion object {
        fun of(walls: Iterable<Iterable<Boolean>>): CompressedMaze {
            val compressedRows = walls.map { CompressedBooleans(it) }
            val sizeX = compressedRows.map { it.size }.distinct().singleOrNull()
                ?: throw NotImplementedError("Rectangular maze expected.")
            return CompressedMaze(compressedRows)
        }

        fun ofPredicate(sizeX: Int, sizeY: Int, isWall: Position.() -> Boolean): CompressedMaze {
            val compressedRows = (0 until sizeY).map { y ->
                CompressedBooleans((0 until sizeX).map { x ->
                    Position(x, y).isWall()
                })
            }
            return CompressedMaze(compressedRows)
        }
    }
}

/**
 * An implementation memory-efficient when the walls are sparse.
 */
class SparseWallRow private constructor(
    // Every list item contains one row.
    // Every row is an ascending list of x-coordinates of walls in this maze row.
    private val rows: List<List<Int>>,
    override val sizeX: Int
) : Maze {
    override val sizeY: Int get() = rows.size

    constructor(walls: Iterable<Iterable<Boolean>>) : this(
        walls.map { row ->
            row.withIndex().filter { it.value }.map { it.index }
        },
        walls.firstOrNull()?.count() ?: 0
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

    override fun isWall(x: Int, y: Int): Boolean?
    = if (x!in indicesX || y!in indicesY) null
    else {
        val position = indicesX.binaryFindLowestWhich { rows[y][x]>= x }
        // rows[y] contains ascending x-coordinates of walls.
        when {
            // No x-coord is >=x
            position == null -> false
            // The lowest x-coord >=x is x
            rows[y][position]== x -> true
            // The lowest x-coord >=x is higher than x
            else -> false
        }
    }
}

