package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.CompressedBooleans
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.binaryFindLowestWhich
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.fail
import kotlin.math.floor
import kotlin.math.roundToInt

class UtilsTest {


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
    fun `fun binaryFindLowestWhich() for small ranges`() {
        for (rangeSize in 0..<100) {
            for (lowestSatisfying in
            // including, because...
            0..rangeSize) {
                runAndVerifyBinarySearch(
                    rangeSize = rangeSize,
                    lowestSatisfying = lowestSatisfying
                )
            }
        }
    }

    fun `fun binaryFindLowestWhich() for large ranges`() {
        val rand = JavaRandom(1)
        for (rangeSize in sparseLargeNumbers(1000)) {
            // A random number to test other than zero-based intervals.
            // Non-positive so that we don't collide with [rangeSize] close to [Int.MAX_VALUE].
            val rangeStart = rand.nextIntIn(-200..0)

            // Comm
            val lowestSatisfying = rangeStart + rand.nextInt(
                (rangeSize * (1 + 0.2)).roundToInt()
            )
            runAndVerifyBinarySearch(
                rangeStart = rangeStart,
                rangeSize = rangeSize,
                lowestSatisfying = lowestSatisfying
            )
        }
    }
}

private fun runAndVerifyBinarySearch(rangeStart: Int = 0, rangeSize: Int, lowestSatisfying: Int) {
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


// ?? Needed?
private fun sparseLargeNumbers(start: Int, multiplier: Double = 1.1): Sequence<Int> = sequence {
    var x: Double = start.toDouble()
    while (x < Int.MAX_VALUE) {
        yield(floor(x).roundToInt())
        x *= multiplier
    }
}

typealias JavaRandom = java.util.Random

fun JavaRandom.nextIntIn(range: ClosedRange<Int>) =
    if (range.isEmpty()) throw IllegalArgumentException("nextIntIn called for an empty range $range")
    else nextInt(range.start, range.endInclusive + 1)

fun JavaRandom.nextBool(probability: Double) = nextDouble() < probability