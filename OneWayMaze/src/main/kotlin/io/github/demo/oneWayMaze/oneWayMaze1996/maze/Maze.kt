package io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1996

/**
 * TTTTTT
 *
 * This interface decouples the computation of the legal paths from a representation of wall list.
 */
interface Maze {
    // sizeX and sizeY are always positive.
    val sizeX: Int
    val sizeY:Int

    /**
     * TTT
     *
     * For out of bounds, returns `null`. Returning `true` might simplify the path-computing code,
     * but it would introduce an unhealthy coupling: If we create this interface, we may as well created
     * it well-defined and independent of the problem where we use it.
     */
    fun isWall(x:Int, y:Int): Boolean?
}

val Maze.indicesX get()= 0..<sizeX
val Maze.indicesY get() = 0..sizeY


