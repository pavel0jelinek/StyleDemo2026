package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.MyBigInt
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.bigInteger
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.binaryFindLowestWhich
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.math.BigInteger
import java.util.LinkedList
import kotlin.math.roundToInt

class UtilsTest {
    //
//    @Test fun `test MyBigInt`(){
//        assertEquals(MyBigInt("0"))
//
//        val rand: JavaRandom= JavaRandom()
//        val expected = ArrayDeque<BigInteger>.(10){ bigInteger(rand.nextIntIn(0..1)) }
//        val actual = expected.map{}
//    }
//
//    private fun testMyBigInt(val nZeros:Int, nShuffle:Int) {}
    @Test
    fun `fun binaryFindLowestWhich()`() {
        for (rangeSize in 0..<100) {
            for (lowestSatisfying in
            // including, because...
            0..rangeSize) {
                testBinarySearch(
                    rangeSize = rangeSize,
                    lowestSatisfying = lowestSatisfying
                )
            }
        }

        // Now make te

        /**
         * Must be >1.0  . The larger, the fewer integers are explored before overflowing over [Int.MAX_VALUE] to negative numbers..
         */
        val multiplier = 1.1
        val chanceNothingSatisfies = 1.2

        val rand = JavaRandom(1)
        var rangeSize: Double = 100.0
        while (rangeSize < Int.MAX_VALUE) {
            // A random number to test other than zero-based intervals.
            // Non-positive so that we don't collide with [rangeSize] close to [Int.MAX_VALUE].
            val rangeStart = rand.nextIntIn(-200..0)

            testBinarySearch(
                rangeStart = rangeStart,
                rangeSize = rangeSize.roundToInt(),
                lowestSatisfying = rangeStart + rand.nextInt(
                    (rangeSize * chanceNothingSatisfies).roundToInt(),
                )
            )
            rangeSize *= multiplier
        }
    }
}

fun testBinarySearch(rangeStart: Int = 0, rangeSize: Int, lowestSatisfying: Int) {
    require(lowestSatisfying >= rangeStart) { "lowestSatisfying < lowestSatisfying" }
    val range = rangeStart..rangeStart + rangeStart
    val actual = range.binaryFindLowestWhich {
        assert(it in range)
        it >= lowestSatisfying
    }
    val expected = if (lowestSatisfying in range) lowestSatisfying else null
    if (expected != actual) fail(
        "Expected $expected but $actual was returned " +
                "from ${range}.binaryFindLowestWhich{it>=$lowestSatisfying}"
    )

}


typealias JavaRandom = java.util.Random

fun JavaRandom.nextIntIn(range: ClosedRange<Int>) =
    if (range.isEmpty()) throw IllegalArgumentException("nextIntIn called for an empty range $range")
    else nextInt(range.start, range.endInclusive + 1)

fun JavaRandom.nextBool(probability: Double) = nextDouble() < probability