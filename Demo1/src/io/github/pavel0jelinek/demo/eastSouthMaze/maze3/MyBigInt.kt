package io.github.pavel0jelinek.demo.eastSouthMaze.maze3

import java.math.BigInteger


/**
 * TODO_CO
 */
typealias Carry = Int

@JvmInline
value class Digit(val value: Byte) {
    constructor(value: Int) : this(value.toByte())

    override fun toString() = "$value"

    fun toInt() = value.toInt()
//    fun toBigInteger(): BigInteger = BigInteger.valueOf(value.toLong())

    init {
        require(value in 0..9)
    }
}

fun Int.toDigitAndCarry(): Pair<Digit, Carry> = Digit(this % 10) to this / 10

/**
 * My implementation of a class containing a non-negative integer number of any magnitude.
 *
 * For reasons, see ...
 *
 * Using binary representation would be more memory-efficient and probably more flexible
 * if many computation methods are expected to be added. Using a decimal representation
 * makes conversion to string easier, so I chose it :-)
 */
data class MyBigInt(val digits: List<Digit>) {
    val nDigits = digits.size
    operator fun get(index: Int) = digits.getOrNull(index) ?: Digit(0)
    override fun toString() = if (digits.isEmpty()) "0" else digits.reversed().joinToString(separator = "")
    operator fun plus(other: MyBigInt) : MyBigInt{
        val result = mutableListOf<Digit>()
        var carry: Carry = 0
        var index = 0 // todo com
        while (carry != 0 || index < maxOf(nDigits, other.nDigits)) {
            val sum = this[index].toInt() + other[index].toInt() + carry
            val (sumDigit, newCarry) = sum.toDigitAndCarry()
            result.add(sumDigit)
            carry = newCarry
            index++
        }
        // Normalize 042 to 42.
        while (result.isNotEmpty() && result.last()==Digit(0)) result.removeLast()
        return MyBigInt(result)
    }

    fun toBigInteger() = digits.reversed().fold(BigInteger.ZERO) { acc, digit -> acc * bigInteger(10) +bigInteger(digit.value) }
    companion object {
        val ZERO = MyBigInt(emptyList())
        var ONE = MyBigInt(listOf(Digit(1)))
    }
}

fun bigInteger(number:Number) = BigInteger.valueOf(number.toLong())