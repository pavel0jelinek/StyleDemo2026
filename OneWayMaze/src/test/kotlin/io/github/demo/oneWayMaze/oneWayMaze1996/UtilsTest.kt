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
        for (rangeSize in generateSequence(300){if (it<Int.MAX_VALUE/3) it*3 else null }) {
            // Define a random number to test other than zero-based intervals.
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


//// ?? Needed?
//private fun sparseLargeNumbers(start: Int, multiplier: Double = 1.1): Sequence<Int> = sequence {
//    var x: Double = start.toDouble()
//    while (x < Int.MAX_VALUE) {
//        yield(floor(x).roundToInt())
//        x *= multiplier
//    }
//}
