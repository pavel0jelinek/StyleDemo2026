package io.github.pavel0jelinek.demo.hanoiTest

import io.github.pavel0jelinek.demo.hanoi.Move
import io.github.pavel0jelinek.demo.hanoi.Rod
import io.github.pavel0jelinek.demo.hanoi.getMovesFunctionally
import io.github.pavel0jelinek.demo.hanoi.getMovesThroughAppending
import io.github.pavel0jelinek.demo.hanoi.getMovesWithoutRecursion
import io.github.pavel0jelinek.demo.hanoi.printMoves
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class HanoiTest {
    // Let's make a little DSL for readable code
    private val A = Rod.A
    private val B = Rod.B
    private val C = Rod.C
    private infix fun Rod.onto(target: Rod) = Move(this, target)

    @Test
    fun verifySmallInstancesAgainstExpectation() {
        assertAllAlgorithmsReturn(1, A onto B)
        assertAllAlgorithmsReturn(2, A onto C, A onto B, C onto B)
        assertAllAlgorithmsReturn(3, A onto B, A onto C, B onto C, A onto B, C onto A, C onto B, A onto B)
    }

    /**
     * Compares the results of the algorithms on larger instances.
     *
     * No "expected moves" are not supplied: the recursive versions are so simple
     * that after passing on small instances (low number of disks),
     * we are confident that they are bug-free.
     */
    @Test
    fun verifyLargeInstancesAgainstOtherAlgorithms() {
        for (nDisks in 4..7) {
            assertAllAlgorithmsReturnSame(nDisks, expectedMoves = null)
        }
    }


    @Test
    fun emptyListReturnedForNonPositiveInput() {
        // Assert that no crash occurs.
        assertAllAlgorithmsReturnSame(-1000, emptyList())
        assertAllAlgorithmsReturnSame(-1, emptyList())
        assertAllAlgorithmsReturnSame(0, emptyList())
    }
}

/**
 * Assert that all algorithms produce the exact same list of moves.
 *
 * If [expectedMoves] is not null, it is also asserted to be equal to these lists.
 */
private fun assertAllAlgorithmsReturnSame(nDisks: Int, expectedMoves: List<Move>?) {
    // Put all versions into a map, associated by readable names.
    val versions = buildMap {
        expectedMoves?.also { put("Expected", it) }
        put("Actual (printing version)", getPrintedContent { printMoves(nDisks) }.parsedToMoves())
        put("Actual (functional style)", getMovesFunctionally(nDisks))
        put("Actual (through appending)", getMovesThroughAppending(nDisks))
        put("Actual (without recursion)", getMovesWithoutRecursion(nDisks))
    }
    versions.entries.zipWithNext { (name1, moves1), (name2, moves2) ->
        if (moves2 != moves1) fail(
            "'$name1' differs from '$name2' for $nDisks discs:" +
                    "\n  $name1: $moves1" +
                    "\n  $name2: $moves2"
        )
    }
}

/**
 * Asserts that all three algorithms produce exactly [moves].
 */
private fun assertAllAlgorithmsReturn(nDisks: Int, vararg moves: Move) {
    assertAllAlgorithmsReturnSame(nDisks, moves.toList())
}

/**
 * Returns all that [block] prints to [System.out], using temporary redirection by [System.setOut].
 */
private fun getPrintedContent(block: () -> Unit): String {
    val original = System.out
    try {
        val buffer = ByteArrayOutputStream()
        System.setOut(PrintStream(buffer))
        block()
        return buffer.toString()
    } finally {
        // Revert to the original.
        // "finally" used because under no circumstances, including an exception,
        // may the redirection stay unreverted.
        System.setOut(original)
    }
}

/** Parses a string into moves under format used by fun [printMoves]. */
private fun String.parsedToMoves(): List<Move> {
    val charToRod = mapOf('A' to Rod.A, 'B' to Rod.B, 'C' to Rod.C)
    return this.filter { it in 'A'..'C' }
        .map { charToRod.getValue(it) }
        .chunked(2)
        .map { (x, y) -> Move(x, y) }
}