package io.github.pavel0jelinek.demo.eastSouthMaze

/**
 * In a rectangular maze (where each tile is a clear tile or obstacle), compute the number of paths from the north-west
 * corner to the south-east one moving only east and south and avoiding the obstacles.
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
fun maze1(obstacles: List<List<Boolean>>, x: Int = obstacles.first().lastIndex, y: Int = obstacles.lastIndex): Long =
    when {
        // Outside the maze
        x < 0 || y < 0 -> 0
        // No legal path enters this obstacle.
        obstacles[y][x] -> 0
        // Starting point
        x == 0 && y == 0 -> 1
        // We can arrive from the west or from the north
        else -> maze1(obstacles, x - 1, y) + maze1(obstacles, x, y - 1)
    }

//
//
///**
// * Returns the value at the position (expecting a non-null),
// * or 0 if [x] or [y] is negative.
// */
//fun Array<Array<HugeInt?>>.read(x: Index, y: Index): HugeInt =
//    if (x < 0 || y < 0) HugeInt.Zero
//    else this[y][x] ?: error("Null encountered at position $x-$y")
//
//fun eastSouthMazeFast(obstacles: List<List<Boolean>>): HugeInt {
//    var paths: Array<Array<HugeInt?>> = obstacles.map { mazeRow ->
//        mazeRow.map { mazeCell ->
//            // Obstacle -> a definite 0, non-obstacle -> we don't know yet
//            if (mazeCell) 0 else null
//        }
//        paths[0][0] = 1
//
//        for (y in paths.indices) {
//            for (x in paths.first().indices) {
//                paths[y][x] = paths[y][x] ?: paths.read(x - 1, y) + paths.read(x, y - 1)
//            }
//        }
//
//        return paths.last().last() ?: error("Cannot be null because we have cycled over all cells.")
//    }
//}
//
//
///** A digit in 0..9 */
//typealias Digit = Short
//typealias Index = Int
//
///**
// * The competition was held in 1995/96 in Pascal or C, where there were no utilities like Java's BigInteger.
// */
//class HugeInt(val digits: List<Digit>) {
//    operator fun get(i: Index) = digits.getOrNull(i) ?: 0
//    override fun toString() = if (digits.isEmpty()) "0" else digits.reversed().toString()
//    operator fun plus(other: HugeInt) = HugeInt(buildList {
//        var i = 0
//        var carry = 0
//        while (true) {
//            var newDigit = carry + this[i] + other[i]
//            if (newDigit == 0) break@while
//            // The carry has been processed.
//                carry = 0
//
//            if (newDigit > 9) {
//                carry = 1
//                newDigit -= 10
//            }
//            add(newDigit)
//        }
//    })
//}
//
//fun main() {
//
//}