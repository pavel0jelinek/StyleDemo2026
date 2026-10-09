package io.github.pavel0jelinek.demo.eastSouthMazeTest

import io.github.pavel0jelinek.demo.eastSouthMaze.Maze
import io.github.pavel0jelinek.demo.eastSouthMaze.maze1
import io.github.pavel0jelinek.demo.eastSouthMaze.maze2a
import io.github.pavel0jelinek.demo.eastSouthMaze.maze2b
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.bigInteger
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.maze3
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.toObstacleRows
import io.github.pavel0jelinek.demo.eastSouthMaze.mazeOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.math.BigInteger

class EastSouthMazeTest {
@Test fun testMaze() {
    mazeOf("""
        ...
        .X.
    """).assertAllReturnSame(expected = bigInteger(1))
    mazeOf("""
        ...
        .X.
        .X.
        ...
    """).assertAllReturnSame(expected = bigInteger(2))

}


}


private fun Maze.assertAllReturnSame(
    expected: BigInteger? = null,
    description: String? = null,
){
    val results:Map<String, BigInteger> = buildMap {
        if (expected!=null){
            put("expected", expected)
        }
        val maze2a = maze2a(obstacles)
        put("maze2a", maze2a)
        put("maze2b", maze2b(this@assertAllReturnSame))
        put("maze3(condensed)", maze3(toObstacleRows(sparse = false)).toBigInteger())
        put("maze3(sparse)", maze3(toObstacleRows(sparse = true)).toBigInteger())
        // Do not run maze1 if its result ([Long]) is doomed to overflow.
        if (maze2a<= bigInteger(Long.MAX_VALUE))
            put("maze1", BigInteger.valueOf(maze1(obstacles)))
    }
    if (results.values.distinct().size>1){
        val message:String = results.entries.joinToString("\n") { (k, v) -> "$v obtained by: $k" } +
                description+"\n\n${this.to2DString()} \n"
        fail(message)
    }

    val grouped: List<Pair<BigInteger, List<String>>> = results.entries.groupBy { it.value }.mapValues { (_, entries): Map.Entry<BigInteger, List<Map.Entry<String, BigInteger>>> -> entries.map{it.key} }.toList()
    if (grouped.size > 1) fail("")


}