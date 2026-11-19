package io.github.pavel0jelinek.styleDemo2026.oneWayMaze

/**
 * In a rectangular maze (where each tile is a clear tile or wall), compute the number of paths from the north-west
 * corner to the south-east one moving only east and south and avoiding the walls.
 *
 * This was part of the republic round of Mathematical Olympian category "P - Programming" which I participated
 * in 1995/96 in my last year of the high school.
 */

/**
 * ################################
 * ## Version 1
 * ################################
 *
 * A trivial solution on 6 lines - very slow (time O(2^minOf(M-1,N-1)) where MxN are the dimensions of the maze.
 *
 * Returns the number of legal paths from the north-west corner to coordinates [x], [y]. The coordinates are zero-based.
 */
fun oneWayMaze1(walls: List<List<Boolean>>, x: Int = walls.first().lastIndex, y: Int = walls.lastIndex): Long =
    when {
        // Outside the maze
        x < 0 || y < 0 -> 0
        // No legal path enters this wall.
        walls[y][x] -> 0
        // Starting point
        x == 0 && y == 0 -> 1
        // We can arrive from the west or from the north
        else -> oneWayMaze1(walls, x - 1, y) + oneWayMaze1(walls, x, y - 1)
    }
