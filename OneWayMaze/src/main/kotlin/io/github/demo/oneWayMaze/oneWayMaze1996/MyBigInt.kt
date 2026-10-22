package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996

import java.math.BigInteger


/**
 * 0 denotes the right-most position (ones).
 * 1 denotes the second-most position (tens)
 * etc.
 */
typealias Significance = Int

/**
 * The value carried over to the next more significant position when the result
 * at the current position does not fit into a single digit (i.e. exceeds 9).
 *
 * When adding two numbers the carry is always 0 or 1 (9 + 9 + 1 = 19).
 */

typealias Carry = Int

@JvmInline
value class Digit(val value: Byte) {
    constructor(value: Int) : this(value.toByte())
    constructor(char: Char) : this(
        if (char.isDigit()) char.code - '0'.code
        else throw IllegalArgumentException("'$char' is not a digit")
    )

    init {
        require(value in 0..9)
    }

    override fun toString() = "$value"

    fun toInt() = value.toInt()
//    fun toBigInteger(): BigInteger = BigInteger.valueOf(value.toLong())


    companion object {
        val ZERO = Digit(0)
    }
}


/**
 * My implementation of a class containing a non-negative integer number of any magnitude.
 *
 * For reasons, see ...
 *
 * Using binary representation would be more memory-efficient and probably more flexible
 * if many computation methods are expected to be added. Using a decimal representation
 * makes conversion to string easier, so I chose it :-)
 */
@ConsistentCopyVisibility
data class MyBigInt private constructor(val digits: List<Digit>) {

    init {
        require(digits.isNotEmpty())
        if (digits.size > 1)
            require(digits.first() != Digit.ZERO) { "Leading zero" }
    }

    val nDigits = digits.size

    override fun toString() = digits.joinToString(separator = "")

    fun digitWithSignificance(significance: Significance):Digit = digits.getOrNull(nDigits-1-significance)
        ?:Digit.ZERO

    /**
     * Returns the Int representation, or throws if `this` is larger than Int.MAX_VALUE.
     */
    fun toInt(): Int = digits.fold(0L) { acc, digit ->
        val next = acc * 10 + digit.toInt()
        if (next > Int.MAX_VALUE) throw ArithmeticException("Value $this does not fit into an Int")
        next
    }.toInt()

    operator fun plus(other: MyBigInt): MyBigInt {
        val result = mutableListOf<Digit>()
        var carry: Carry = 0
        // least significant digit first
        var significance: Significance = 0
        val length = maxOf(nDigits, other.nDigits)

        while (carry != 0 || significance < length) {
            val sum = this.digitWithSignificance(significance).toInt()+
            other.digitWithSignificance(significance).toInt()+
                    carry
            result.addLast(Digit(sum % 10))
            carry = sum / 10
            significance++
        }

        return fromDigits(result.reversed())
    }

    companion object {
        val ZERO = MyBigInt(listOf(Digit.ZERO))
        val ONE = MyBigInt(listOf(Digit(1)))

        fun fromDigits(digits: Iterable<Digit>): MyBigInt {
            val trimmed = digits.dropWhile { it == Digit.ZERO }
            return if (trimmed.isEmpty()) ZERO else MyBigInt(trimmed)
        }

        fun fromString(asString: String): MyBigInt {
            val digits = asString.filterNot { it.isWhitespace() }.map{ Digit(it) }
            require(digits.isNotEmpty()){"String \"$asString\" does not contain digits."}
            return fromDigits(digits)
        }
    }
}

//fun bigInteger(number: Number): BigInteger = BigInteger.valueOf(number.toLong())
//
//// Kam??
//fun productOf(numbers: Iterable<Int>): BigInteger = numbers.fold(BigInteger.ONE) { acc, i -> acc * bigInteger(i) }
//
//fun factorial(base: Int): BigInteger = productOf(2..base) //??needed??
//
//// Comm
//fun numberOfSubsets(sizeOfSet: Int, sizeOfSubSet: Int): BigInteger = when {
//    sizeOfSubSet > sizeOfSet -> BigInteger.ZERO
//    else -> productOf((sizeOfSet - sizeOfSubSet + 1)..sizeOfSet) / productOf(1..sizeOfSubSet)
//}
