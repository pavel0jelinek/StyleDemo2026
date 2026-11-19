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

 * Claude radi:
 *
 * Testovany objekt je zaroven oracle. assertEquals(myBigInt(expected), a + b) spoleha na equals. Pokud je equals rozbity (treba vzdy true), vsechno projde. Porovnavej take toString(): assertEquals(expected, (a + b).toString()). U testu s leading zeros stejne.
 * Test s 100 000 cislicemi:
 * Pri selhani assertEquals vypise dva retezce o 100 000 znacich. Pouzij assertTrue(x == y) nebo porovnej jen delku a hash.
 * Limit 5 s muze byt flaky na pomalem CI (JIT warm-up), a naopak kvadratickou slozitost nemusi odhalit. Lepsi je zmerit 100k a 200k a overit, ze pomer casu je blizko 2, ne
 *
 * assertTimeoutPreemptively bezi v jinem vlakne s jinou velikosti zasobniku, takze test na stack overflow nemusi verne odpovidat beznemu pouziti.
 *
 * Neotestovana nemutabilita: po a + b se nesmi zmenit a ani b. Staci si pred scitanim ulozit toString() obou operandu a po nem je porovnat.
 * Chybejici vlastnosti scitani: asociativita (a+b)+c == a+(b+c) a a + ZERO == a. Do nahodneho testu se pridaji za par radku.
 *
 * ### Prezentace (pro nekoho, kdo to bude cist jako ukazku prace)
 * Uvodni komentar ("using AI is imho the only way how to be productive") je zbytecne provokativni a v demu odvadi pozornost od toho, co umis. Mnohem silnejsi je neutralni a konkretni verze: "Zaklad testu vznikl s pomoci AI, ja jsem je zrevidoval, opravil X a doplnil Y." Tester, ktery kriticky proveri vystup AI, je presne ten typ, ktereho chces ukazat. Zmin konkretne, co jsi v AI navrhu nasel nebo doplnil.
 * KDoc je u private fun myBigInt, ne u tridy. Patri nad class MyBigIntTest.
 * Zbytky v kodu: // PJ_, // PJ_TODO_KEEP?, preklep "from a scratch", nekonzistentni formatovani ("0","1", "2" ,"7"), chybi prazdne radky, smycka for bez slozenych zavorek obaluje repeat, stray ] v komentari.
 * Package a importy nesedi (test.kotlin.io.github.demo... vs. io.github.pavel0jelinek.StyleDemo2026...). Test obvykle lezi ve stejnem balicku jako testovana trida.
 * nextBool je nepouzita.
 *
 * ### Co by dodalo "wow" efekt
 * Mutacni testovani (PIT): ukazes, ze tvoje testy opravdu zachyti zamerne vlozene chyby, ne jen ze "prochazi". Jedna veta ve zprave typu "mutation score 9x %" vazi vic nez dalsich 50 radku CSV.
 * Pokryti (JaCoCo) jako doplnek.
 * Property-based pristup (jqwik/Kotest), pripadne aspon zminka, proc jsi zvolil rucni nahodny generator s pevnym seedem (reprodukovatelnost). Ten duvod v kodu chybi, a pritom je dobry. Do chybove hlasky pridej i seed.
 * Kratky README s tabulkou: co je testovano, jakou technikou (ekvivalencni tridy, hranicni hodnoty, nahodne testy proti referencnimu oracle, vykonnostni test) a jake chyby se tim daly najit.
 *
 * Pokud chces, projdu s tebou i samotnou implementaci MyBigInt a navrhnu, jake chyby by tyto testy jeste neodhalily.
 */



/**
 * This test class was created from a scratch supplied by Claude AI, because using AI
 * is imho the only way how to be productive as a programmer.
 */
private fun myBigInt(s: String) = MyBigInt.fromString(s)
private fun myBigInt(i: Int) = MyBigInt.fromString(i.toString()) // PJ_
private fun MyBigInt.toBigInteger() // PJ_TODO_KEEP?
= BigInteger(this.toString())

class MyBigIntTest {

    // --- Parsing -----------------------------------------------------------

    @ParameterizedTest
    @ValueSource(strings = ["0","1", "2" ,"7", "42", "1000", "123456789012345678901234567890"])
    fun `toString returns the original number`(s: String) {
        assertEquals(s, myBigInt(s).toString())
    }

    @Test
    fun `leading zeros are stripped`() {
        assertEquals(myBigInt("0042"), myBigInt("42"))
        assertEquals(myBigInt("0000"), myBigInt("0"))
    }

    @Test
    fun `toString works by spec`() {
        assertEquals("42", myBigInt("0042").toString())
        assertEquals("0", myBigInt("0000").toString())
    }


    @Test
    fun `surrounding whitespace is ignored`() {
        assertEquals(myBigInt("123"), myBigInt("  123\n"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "   ", "12e3", "12e+3", "+5", "-5", "1.5", "1.000", "1_000", "1,000"])
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

    @Test
    fun `toInt() returns the int representation`() {
        assertEquals(0, myBigInt("0").toInt())
        assertEquals(789, myBigInt("789").toInt())
        assertThrows<ArithmeticException> { myBigInt("10 000 000").toInt() }
    }


    // --- Addition ----------------------------------------------------------
    @ParameterizedTest(name = "Addition: {0} + {1} = {2}")
    @CsvSource(
        // This detailed test set was produced by Claude chatbot.

        // Zero is the identity element; also check zero together with a multi-chunk number.
        "0, 0, 0",
        "0, 123, 123",
        "123456789012345678901234567890, 0, 123456789012345678901234567890",

        // Single-digit operands, without and with carry out of the only digit.
        "1, 2, 3",
        "9, 1, 10",
        "9, 9, 18",

        // Carry propagation: a carry ripples through several digits and grows the result.
        "99, 1, 100",
        "999, 1, 1000",
        "123, 877, 1000",
        "55555, 55555, 111110",
        "999999, 999999, 1999998",

        // A carry must stop where it is absorbed, and must not leak into higher digits.
        "199, 1, 200",
        "1099, 1, 1100",
        "99990000, 10000, 100000000",

        // Operands of different length (the shorter one must be padded implicitly).
        "5, 12345, 12350",
        "1, 99999, 100000",
        "1000000, 1, 1000001",
        "123456789012345678901234567890, 1, 123456789012345678901234567891",

        // Boundaries of likely internal chunk sizes (base 10^4, 10^9, 10^18).
        "9999, 1, 10000",
        "999999999, 1, 1000000000",
        "1000000000, 1000000000, 2000000000",
        "999999999999999999, 1, 1000000000000000000",

        // Boundaries of primitive types, in case the implementation uses Int/Long internally.
        "2147483647, 1, 2147483648",
        "2147483647, 2147483647, 4294967294",
        "4294967295, 1, 4294967296",
        "9223372036854775807, 1, 9223372036854775808",
        "9223372036854775807, 9223372036854775807, 18446744073709551614",
        "18446744073709551615, 1, 18446744073709551616",

        // Numbers far beyond Long; zeros inside the numbers must survive the addition.
        "111111111011111111101111111110, 222222222022222222202222222220, 333333333033333333303333333330",
        "12345678901234567890, 98765432109876543210, 111111111011111111100",
        "100000000000000000001, 99999999999999999999, 200000000000000000000",

        // Carry through all digits of the number, with a new leading digit at the end.
        "99999999999999999999, 1, 100000000000000000000",
        "999999999999999999999999999999, 1, 1000000000000000000000000000000",
        "99999999999999999999, 99999999999999999999, 199999999999999999998",
    )
    fun `adds correctly`(a: String, b: String, expected: String) {
        assertEquals(myBigInt(expected), myBigInt(a) + myBigInt(b))
        assertEquals(myBigInt(expected), myBigInt(b) + myBigInt(a))
    }

    @Test
    fun `addition of very long numbers`() {
        // 555555... = 222222... + 333333...
        assertEquals(myBigInt("5".repeat(1000)), myBigInt("2".repeat(1000)) + myBigInt("3".repeat(1000)))
        // 1000000... = 1 + 999999...
        assertEquals(myBigInt("1" + "0".repeat(1000)), MyBigInt.ONE + myBigInt("9".repeat(1000)))
    }

    /**
     * 100_000 digits: This should produce
     * - a stack overflow error in case of a badly designed recursive implementation.
     * - an extreme running time in case of a quadratic complexity.
     */
    @Test
    fun `addition of extreme numbers`() {
        assertTimeoutPreemptively(Duration.ofSeconds(5)) {
            assertEquals(myBigInt("2".repeat(100_000)), myBigInt("1".repeat(100_000)) + myBigInt("1".repeat(100_000)))
        }

    }

    @Test
    fun `systematic addition`() {
        // An asymmetric range gives a better ratio between total cases and occurrence of interesting (possibly edge) cases.
        for (a in 0..11)
            for (b in 0..111) {
                assertEquals(myBigInt(a+b), myBigInt(a) + myBigInt(b), "$a+$b")
            }
    }

    /**
     * Package oneWayMaze1996 demonstrates how to solve One Way Maze problem
     * without using (the most significant) tools that were not available in 1996.
     *
     * Therefore, it would make some sense to replace this test by another - using Long or List<Digit>. See `random addition (Long)`
     *
     */
    @Test
    fun `random addition (BigInteger)`{
        val rand = JavaRandom(1)
        val digitGenerators = listOf<() -> Digit>(
            // Random digits
            { Digit(rand.nextIntIn(0..9))},
            // Digits producing more chances for edge cases.
            { Digit(listOf(0,1,9).randomElement(rand))}
        )
        for (digitGenerator in digitGenerators) {
            repeat(1000) {
                val operands = List(2) {
                    // This distribution produces a high length sometimes but a reasonably short length more often,
                    // leading to an (imho) good ratio between the computation time and number of cases explored.
                    // Some length will never occur (such as large primes), but this fact does not skip any probable edge case.
                    val length = rand.nextIntIn(3..30) * rand.nextIntIn(3..30)
                    MyBigInt.fromDigits(List(length){digitGenerator()})
                }
                val actual = (operands[0] + operands[1]).toBigInteger()
                val expected = operands[0].toBigInteger() + operands[1].toBigInteger()
                assertEquals(expected, actual, operands.joinToString(" + "))
            }
        }
    }

    /**
     * Redundant if we do use `random addition (BigInteger)`].
     */
    @Test
    fun `random addition (Long)`{
        val rand = JavaRandom(1)
        repeat(1000) {
            val operands = List(2) { rand.nextLong(Long.MAX_VALUE / 2) }
            ...
        }
    }


}


typealias JavaRandom = java.util.Random

fun JavaRandom.nextIntIn(range: ClosedRange<Int>) =
    if (range.isEmpty()) throw IllegalArgumentException("nextIntIn called for an empty range $range")
    else nextInt(range.start, range.endInclusive + 1)

fun JavaRandom.nextBool(probability: Double) = nextDouble() < probability
fun JavaRandom.nextDigit() = Digit(nextInt(10))

fun <E> List<E>.randomElement(rand: JavaRandom):E = if (isEmpty()) throw NoSuchElementException() else this[rand.nextInt(this.size)]