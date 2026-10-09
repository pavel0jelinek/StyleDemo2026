package io.github.pavel0jelinek.demo.eastSouthMaze.maze3


/**
 * TODO_CO
 */
typealias Carry = Int

@JvmInline
value class Digit(val value: Byte) {
    constructor(value: Int) : this(value.toByte())

    override fun toString() = "$value"

    fun toInt() = value.toInt()

    init {
        require(value in 0..9)
    }
}

fun Int.toDigitAndCarry(): Pair<Digit, Carry> = Digit(this % 10) to this / 10

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
    companion object {
        val ZERO = MyBigInt(emptyList())
        var ONE = MyBigInt(listOf(Digit(1)))
    }
}