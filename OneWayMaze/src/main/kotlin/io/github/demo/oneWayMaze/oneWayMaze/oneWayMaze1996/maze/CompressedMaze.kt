package io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.styleDemo2026.oneWayMaze.Position
import io.github.pavel0jelinek.styleDemo2026.oneWayMaze.maze.CompressedBooleans


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