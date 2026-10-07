package io.github.pavel0jelinek.demo.eastSouthMazeTest

import io.github.pavel0jelinek.demo.eastSouthMaze.Maze
import io.github.pavel0jelinek.demo.eastSouthMaze.mazeDefinedBy
import io.github.pavel0jelinek.demo.eastSouthMaze.maze1
import io.github.pavel0jelinek.demo.eastSouthMaze.maze2a
import io.github.pavel0jelinek.demo.eastSouthMaze.maze2b
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.bigInteger
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.numberOfSubsets
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.maze3
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.toObstacleRows
import io.github.pavel0jelinek.demo.eastSouthMaze.mazeOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.math.BigInteger

class EastSouthMazeTest {
    @Test
    fun mazes1x1() {
        mazeOf(
            """
        .        
    """
        ).assertAllReturnSame(expected = 1)
        mazeOf(
            """
        X        
    """
        ).assertAllReturnSame(expected = 0)
    }

    @Test
    fun mazes3x3() {

        mazeOf(
            """
        X..
        ...
        ..X
    """
        ).assertAllReturnSame(0)
        mazeOf(
            """
        ..X
        ...
        X..
    """
        ).assertAllReturnSame(4, "Obligatory passage through 1-1; 2 ways to get there x 2 ways to continue")
    }


    @Test
    fun emptyMazes() {
        for (sizeX in listOf(1, 2, 3, 5, 10)) {
            for (sizeY in listOf(1, 2, 3, 5, 10, 20, 50)) {
                // The path has (sizeX+sizeY-2). Of those, (sizeX-1) will be south.
                // Each path in an empty maze can be uniquely described by a (sizeX-1) subset of a
                // (sizeX+sizeY-2) set (let's say, of set 0..<(sizeX+sizeY-2)); these subsets are the indices of steps south.
                val expected = numberOfSubsets(sizeX + sizeY - 2, sizeX - 1)
                mazeDefinedBy(sizeX, sizeY) { false }.assertAllReturnSame(expected)
            }
        }
    }


}


private fun Maze.assertAllReturnSame(
    expected: Int,
    description: String? = null,
) = assertAllReturnSame(bigInteger(expected), description, includingSlowAlgorithm = true)

private fun Maze.assertAllReturnSame(
    expected: BigInteger? = null,
    description: String? = null,
    includingSlowAlgorithm: Boolean = false
) {
    val results: Map<String, BigInteger> = buildMap {
        if (expected != null) put("expected", expected)
        if (includingSlowAlgorithm) put("by maze1", BigInteger.valueOf(maze1(obstacles)))
        put("by maze2a", maze2a(obstacles))
        put("by maze2b", maze2b(this@assertAllReturnSame))
        put("by maze3(condensed)", maze3(toObstacleRows(sparse = false)).toBigInteger())
        put("by maze3(sparse)", maze3(toObstacleRows(sparse = true)).toBigInteger())

    }
    if (results.values.distinct().size > 1) {
        val message: String = results.entries.joinToString("\n") { (k, v) -> "$v = $k" } +
                description + "\n\n${this.to2DString()} \n"
        fail(message)
    }

    val grouped: List<Pair<BigInteger, List<String>>> = results.entries.groupBy { it.value }
        .mapValues { (_, entries): Map.Entry<BigInteger, List<Map.Entry<String, BigInteger>>> -> entries.map { it.key } }
        .toList()
    if (grouped.size > 1) fail("")


}


fun main() {
    mazeDefinedBy(20, 20) { false }.assertAllReturnSame()

}