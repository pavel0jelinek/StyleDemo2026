package io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1996.binaryFindLowestWhich

/**
 * An implementation memory-efficient when the walls are sparse.
 */
class SparseWallMaze private constructor(
    // Every list item contains one row.
    // Every row is an ascending list of x-coordinates of walls in this maze row.
    private val rows: List<List<Int>>,
    override val sizeX: Int
) : Maze {
    override val sizeY: Int get() = rows.size

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

    companion object{
        fun of(walls: Iterable<Iterable<Boolean>>)  = SparseWallMaze(
        walls.map { row ->
            row.withIndex().filter { it.value }.map { it.index }
        },
        walls.firstOrNull()?.count() ?: 0
        )
    }
}