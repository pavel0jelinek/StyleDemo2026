package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.CompressedBooleans
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.CompressedMaze
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.Maze
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.SparseWallMaze
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.indicesX
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.indicesY
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class MazeTest {


    @Test
    fun `Test CompressedBooleans()`() {
        val rand = JavaRandom(1)
        for (size in (0..100) + listOf(127, 128, 129, 1000, 10_000, 100_000)) {
            for (bools in listOf(
                List(size) { false },
                List(size) { rand.nextBoolean() },
                List(size) { true },
            )) {
                val cb = CompressedBooleans(bools)
                assertEquals(size, cb.size, "cb.size")
                assertEquals(bools, cb.toBools(), "cb.toBools()")
                for (i in bools.indices) {
                    assertEquals(bools[i], cb[i], "cb.get($i)")
                }

                for (outlyingIndex in listOf(Int.MIN_VALUE, -1, size, size + 1, Int.MAX_VALUE)) {
                    assertNull(
                        cb[outlyingIndex],
                        "$size-sized CompressedBooleans should return null at index $outlyingIndex"
                    )
                }
            }
        }
    }

    @Test
    fun testMazes() {
        val rand = JavaRandom(1)
        for (sizeX in (1..20) + listOf(50, 100, 200, 500)) {
            for (sizeY in listOf(1, 2, 3, 4, 5, 10)) {
                for (generator: () -> Boolean in listOf({ false }, { true }, { rand.nextBoolean() })) {
                    val bools = List(sizeY) { List(sizeX) { generator() } }
                    verifyMaze(bools, CompressedMaze.of(bools), SparseWallMaze.of(bools))
                }
            }
        }
    }

    private fun verifyMaze(
        bools: List<List<Boolean>>,
        vararg mazes: Maze,
    ) {
        for (maze in mazes) {
            assertEquals(bools.first().size, maze.sizeX)
            assertEquals(bools.size, maze.sizeY)
            for (x in -1..maze.sizeX) {
                for (y in -1..maze.sizeY) {
                    val expected: Boolean? = if (x in maze.indicesX && y in maze.indicesY) bools[y][x]
                    // Out of bounds
                    else null
                    assertEquals(expected, maze.isWall(x,y))
                }
            }
        }
    }
}