package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996

import io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996.MyBigInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigInteger
import kotlin.random.Random

/**
 * This test class was created from a scratch supplied by Claude AI, because using AI
 * is imho the only way how to be productive as a programmer.
 */
private fun myBigInt(s: String) = MyBigInt.fromString(s)
private fun MyBigInt.toBigInteger() = BigInteger(this.toString())

class MyBigIntTest {

    // --- Parsing -----------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = ["0", "7", "42", "1000", "123456789012345678901234567890"])
    fun `toString returns the original number`(s: String) {
        assertEquals(s, myBigInt(s).toString())
    }

    @Test
    fun `leading zeros are stripped`() {
        assertEquals(myBigInt("0042"), myBigInt("42"))
        assertEquals(myBigInt("0000"), myBigInt("0"))

    }

    @Test
    fun `surrounding whitespace is ignored`() {
        assertEquals(myBigInt("123"), myBigInt("  123\n"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "   ", "12a3", "-5", "1.5"])
    fun `invalid input is rejected`(s: String) {
        assertThrows<IllegalArgumentException> { myBigInt(s) }
    }

    @Test
    fun `constants have expected values`() {
        assertEquals("0", MyBigInt.ZERO.toString())
        assertEquals("1", MyBigInt.ONE.toString())
    }
    // --- Basics ------------------------------------------------------------

    @Test
    fun `nDigits counts digits`() {
        assertEquals(1, MyBigInt.ZERO.nDigits)
        assertEquals(4, myBigInt("1234").nDigits)
    }

    fun `toInt() returns the int representation`(){
        assertEquals(0, myBigInt("0").toInt())
        assertEquals(789, myBigInt("789").toInt())
        assertThrows<ArithmeticException> { myBigInt("10_000_000").toInt() }
    }


    // --- Addition ----------------------------------------------------------

    @ParameterizedTest(name = "Addition: {0} + {1} = {2}")
    @CsvSource(
        "0, 0, 0",
        "0, 123, 123",
        "1, 2, 3",
        "9, 1, 10",
        "99, 1, 100",
        "999, 1, 1000",
        "123, 877, 1000",
        "5, 12345, 12350",
        "111111111011111111101111111110", "222222222022222222202222222220", "333333333033333333303333333330",
        "99999999999999999999, 1, 100000000000000000000",
        "12345678901234567890, 98765432109876543210, 111111111011111111100",
    )
    fun `adds correctly`(a: String, b: String, expected: String) {
        assertEquals(myBigInt(expected), myBigInt(a) + myBigInt(b))
        assertEquals(myBigInt(expected), myBigInt(b) + myBigInt(a))
    }

    fun `very long addition`() {
        assertEquals(myBigInt("5".repeat(1000)), myBigInt("2".repeat(1000)) + myBigInt("3".repeat(1000)))
        assertEquals(myBigInt("1" + "0".repeat(1000)), MyBigInt.ONE + myBigInt("9".repeat(1000)))
    }
}
