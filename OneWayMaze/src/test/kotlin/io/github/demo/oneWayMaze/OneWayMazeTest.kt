//package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze
//
//import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.mazeFromPredicate
//import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1
//import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.bigInteger
//import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.numberOfSubsets
//import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.mazeOf
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.fail
//import java.math.BigInteger
//
//class OneWayMazeTest {
//    @Test
//    fun mazes1x1() {
//        mazeOf(
//            """
//        .
//    """
//        ).assertAllReturnSame(expected = 1)
//        mazeOf(
//            """
//        X
//    """
//        ).assertAllReturnSame(expected = 0)
//    }
//
//    @Test
//    fun mazes3x3() {
//
//        mazeOf(
//            """
//        X..
//        ...
//        ..X
//    """
//        ).assertAllReturnSame(0)
//        mazeOf(
//            """
//        ..X
//        ...
//        X..
//    """
//        ).assertAllReturnSame(4, "Obligatory passage through 1-1; 2 ways to get there x 2 ways to continue")
//    }
//
//
//    @Test
//    fun emptyMazes() {
//        for (sizeX in listOf(1, 2, 3, 5, 10)) {
//            for (sizeY in listOf(1, 2, 3, 5, 10, 20, 50)) {
//                // The path has (sizeX+sizeY-2). Of those, (sizeX-1) will be south.
//                // Each path in an empty maze can be uniquely described by a (sizeX-1) subset of a
//                // (sizeX+sizeY-2) set (let's say, of set 0..<(sizeX+sizeY-2)); these subsets are the indices of steps south.
//                val expected = numberOfSubsets(sizeX + sizeY - 2, sizeX - 1)
//                mazeFromPredicate(sizeX, sizeY) { false }.assertAllReturnSame(expected)
//            }
//        }
//    }
//
//
//}
//
//
//private fun BooleanMaze.assertAllReturnSame(
//    expected: Int,
//    description: String? = null,
//) = assertAllReturnSame(bigInteger(expected), description, includingSlowAlgorithm = true)
//
//private fun BooleanMaze.assertAllReturnSame(
//    expected: BigInteger? = null,
//    description: String? = null,
//    includingSlowAlgorithm: Boolean = false
//) {
//    val results: Map<String, BigInteger> = buildMap {
//        if (expected != null) put("expected", expected)
//        if (includingSlowAlgorithm) put("by maze1", BigInteger.valueOf(oneWayMaze1(walls)))
//        put("by maze2a", oneWayMaze2a(walls))
//        put("by maze2b", oneWayMaze2b(this@assertAllReturnSame))
//        put("by maze3(condensed)", oneWayMaze1996(toWallRows(sparse = false)).toBigInteger())
//        // It is tempting to add also the following line, but it is not clean, because
//        // the correct functionality of individual [Maze] implementations should be tested separately.
//        // This is not the unit test for Comp xxxxx
//        put("by maze3(sparse)", oneWayMaze1996(toWallRows(sparse = true)).toBigInteger())
//
//    }
//    if (results.values.distinct().size > 1) {
//        val message: String = results.entries.joinToString("\n") { (k, v) -> "$v = $k" } +
//                description + "\n\n${this.to2DString()} \n"
//        fail(message)
//    }
//
//    val grouped: List<Pair<BigInteger, List<String>>> = results.entries.groupBy { it.value }
//        .mapValues { (_, entries): Map.Entry<BigInteger, List<Map.Entry<String, BigInteger>>> -> entries.map { it.key } }
//        .toList()
//    if (grouped.size > 1) fail("")
//
//
//}
//
//
//fun main() {
//    mazeFromPredicate(20, 20) { false }.assertAllReturnSame()
//
//}