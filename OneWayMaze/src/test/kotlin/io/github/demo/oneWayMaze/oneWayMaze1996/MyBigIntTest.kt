package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.Digit
import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.MyBigInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.assertTimeoutPreemptively
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigInteger
import java.time.Duration
import kotlin.random.Random


//
///**
// * This test class was created from a scratch supplied by Claude AI, because using AI
// * is imho the only way how to be productive as a programmer.
// */
//private fun myBigInt(s: String) = MyBigInt.fromString(s)
//private fun myBigInt(i: Int) = MyBigInt.fromString(i.toString()) // PJ_
//private fun MyBigInt.toBigInteger() // PJ_TODO_KEEP?
//= BigInteger(this.toString())
//
//class MyBigIntTest {
//
//    // --- Parsing -----------------------------------------------------------
//
//    @ParameterizedTest
//    @ValueSource(strings = ["0","1", "2" ,"7", "42", "1000", "123456789012345678901234567890"])
//    fun `toString returns the original number`(s: String) {
//        assertEquals(s, myBigInt(s).toString())
//    }
//
//    @Test
//    fun `leading zeros are stripped`() {
//        assertEquals(myBigInt("0042"), myBigInt("42"))
//        assertEquals(myBigInt("0000"), myBigInt("0"))
//    }
//
//    @Test
//    fun `toString works by spec`() {
//        assertEquals("42", myBigInt("0042").toString())
//        assertEquals("0", myBigInt("0000").toString())
//    }
//
//
//    @Test
//    fun `surrounding whitespace is ignored`() {
//        assertEquals(myBigInt("123"), myBigInt("  123\n"))
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = ["", "a", "   ", "12e3", "12e+3", "+5", "-5", "1.5", "1.000", "1_000", "1,000"])
//    fun `invalid input is rejected`(s: String) {
//        assertThrows<IllegalArgumentException> { myBigInt(s) }
//    }
//
//    @Test
//    fun `constants have expected values`() {
//        assertEquals("0", MyBigInt.ZERO.toString())
//        assertEquals("1", MyBigInt.ONE.toString())
//    }
//    // --- Basics ------------------------------------------------------------
//
//    @Test
//    fun `nDigits counts digits`() {
//        assertEquals(1, MyBigInt.ZERO.nDigits)
//        assertEquals(4, myBigInt("1234").nDigits)
//    }
//
//    @Test
//    fun `toInt() returns the int representation`() {
//        assertEquals(0, myBigInt("0").toInt())
//        assertEquals(789, myBigInt("789").toInt())
//        assertThrows<ArithmeticException> { myBigInt("10 000 000").toInt() }
//    }
//
//
//    // --- Addition ----------------------------------------------------------
//    @ParameterizedTest(name = "Addition: {0} + {1} = {2}")
//    @CsvSource(
//        // This detailed test set was produced by Claude chatbot.
//
//        // Zero is the identity element; also check zero together with a multi-chunk number.
//        "0, 0, 0",
//        "0, 123, 123",
//        "123456789012345678901234567890, 0, 123456789012345678901234567890",
//
//        // Single-digit operands, without and with carry out of the only digit.
//        "1, 2, 3",
//        "9, 1, 10",
//        "9, 9, 18",
//
//        // Carry propagation: a carry ripples through several digits and grows the result.
//        "99, 1, 100",
//        "999, 1, 1000",
//        "123, 877, 1000",
//        "55555, 55555, 111110",
//        "999999, 999999, 1999998",
//
//        // A carry must stop where it is absorbed, and must not leak into higher digits.
//        "199, 1, 200",
//        "1099, 1, 1100",
//        "99990000, 10000, 100000000",
//
//        // Operands of different length (the shorter one must be padded implicitly).
//        "5, 12345, 12350",
//        "1, 99999, 100000",
//        "1000000, 1, 1000001",
//        "123456789012345678901234567890, 1, 123456789012345678901234567891",
//
//        // Boundaries of likely internal chunk sizes (base 10^4, 10^9, 10^18).
//        "9999, 1, 10000",
//        "999999999, 1, 1000000000",
//        "1000000000, 1000000000, 2000000000",
//        "999999999999999999, 1, 1000000000000000000",
//
//        // Boundaries of primitive types, in case the implementation uses Int/Long internally.
//        "2147483647, 1, 2147483648",
//        "2147483647, 2147483647, 4294967294",
//        "4294967295, 1, 4294967296",
//        "9223372036854775807, 1, 9223372036854775808",
//        "9223372036854775807, 9223372036854775807, 18446744073709551614",
//        "18446744073709551615, 1, 18446744073709551616",
//
//        // Numbers far beyond Long; zeros inside the numbers must survive the addition.
//        "111111111011111111101111111110, 222222222022222222202222222220, 333333333033333333303333333330",
//        "12345678901234567890, 98765432109876543210, 111111111011111111100",
//        "100000000000000000001, 99999999999999999999, 200000000000000000000",
//
//        // Carry through all digits of the number, with a new leading digit at the end.
//        "99999999999999999999, 1, 100000000000000000000",
//        "999999999999999999999999999999, 1, 1000000000000000000000000000000",
//        "99999999999999999999, 99999999999999999999, 199999999999999999998",
//    )
//    fun `adds correctly`(a: String, b: String, expected: String) {
//        assertEquals(myBigInt(expected), myBigInt(a) + myBigInt(b))
//        assertEquals(myBigInt(expected), myBigInt(b) + myBigInt(a))
//    }
//
//    @Test
//    fun `addition of very long numbers`() {
//        // 555555... = 222222... + 333333...
//        assertEquals(myBigInt("5".repeat(1000)), myBigInt("2".repeat(1000)) + myBigInt("3".repeat(1000)))
//        // 1000000... = 1 + 999999...
//        assertEquals(myBigInt("1" + "0".repeat(1000)), MyBigInt.ONE + myBigInt("9".repeat(1000)))
//    }
//
//    /**
//     * 100_000 digits: This should produce
//     * - a stack overflow error in case of a badly designed recursive implementation.
//     * - an extreme running time in case of a quadratic complexity.
//     */
//    @Test
//    fun `addition of extreme numbers`() {
//        assertTimeoutPreemptively(Duration.ofSeconds(5)) {
//            assertEquals(myBigInt("2".repeat(100_000)), myBigInt("1".repeat(100_000)) + myBigInt("1".repeat(100_000)))
//        }
//
//    }
//
//    @Test
//    fun `systematic addition`() {
//        // An asymmetric range gives a better ratio between total cases and occurrence of interesting (possibly edge) cases.
//        for (a in 0..11)
//            for (b in 0..111) {
//                assertEquals(myBigInt(a+b), myBigInt(a) + myBigInt(b), "$a+$b")
//            }
//    }
//
//    /**
//     * Package oneWayMaze1996 demonstrates how to solve One Way Maze problem
//     * without using (the most significant) tools that were not available in 1996.
//     *
//     * Therefore, it would make some sense to replace this test by another - using Long or List<Digit>. See `random addition (Long)`
//     *
//     */
//    @Test
//    fun `random addition (BigInteger)`{
//        val rand = JavaRandom(1)
//        val digitGenerators = listOf<() -> Digit>(
//            // Random digits
//            { Digit(rand.nextIntIn(0..9))},
//            // Digits producing more chances for edge cases.
//            { Digit(listOf(0,1,9).randomElement(rand))}
//        )
//        for (digitGenerator in digitGenerators) {
//            repeat(1000) {
//                val operands = List(2) {
//                    // This distribution produces a high length sometimes but a reasonably short length more often,
//                    // leading to an (imho) good ratio between the computation time and number of cases explored.
//                    // Some length will never occur (such as large primes), but this fact does not skip any probable edge case.
//                    val length = rand.nextIntIn(3..30) * rand.nextIntIn(3..30)
//                    MyBigInt.fromDigits(List(length){digitGenerator()})
//                }
//                val actual = (operands[0] + operands[1]).toBigInteger()
//                val expected = operands[0].toBigInteger() + operands[1].toBigInteger()
//                assertEquals(expected, actual, operands.joinToString(" + "))
//            }
//        }
//    }
//
//    /**
//     * Redundant if we do use `random addition (BigInteger)`].
//     */
//    @Test
//    fun `random addition (Long)`{
//        val rand = JavaRandom(1)
//        repeat(1000) {
//            val operands = List(2) { rand.nextLong(Long.MAX_VALUE / 2) }
//            ...
//        }
//    }
//
//
//}
//
//
typealias JavaRandom = java.util.Random

fun JavaRandom.nextIntIn(range: ClosedRange<Int>) =
    if (range.isEmpty()) throw IllegalArgumentException("nextIntIn called for an empty range $range")
    else nextInt(range.start, range.endInclusive + 1)

fun JavaRandom.nextBool(probability: Double) = nextDouble() < probability
fun JavaRandom.nextDigit() = Digit(nextInt(10))

fun <E> List<E>.randomElement(rand: JavaRandom):E = if (isEmpty()) throw NoSuchElementException() else this[rand.nextInt(this.size)]