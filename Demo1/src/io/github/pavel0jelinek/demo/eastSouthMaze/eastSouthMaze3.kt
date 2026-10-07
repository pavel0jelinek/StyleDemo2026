package io.github.pavel0jelinek.demo.eastSouthMaze

import kotlin.collections.buildList

/**
 * The competition where I encountered this problem was held in 1995/96, we programmed in Pascal or C.
 * There were no such things like BigInteger or MutableMap, if we did not implement them ourselves.
 *
 * Therefore, I will implement the solution without these two utilities, but still with all the convenience offered by Kotlin.
 */

//typealias Carry = Int
//
//@JvmInline
//value class Digit(val value: Byte) {
//    constructor(value: Int) : this(value.toByte())
//
//    override fun toString() = "$value"
//
//    fun toInt() = value.toInt()
//
//    init {
//        require(value in 0..9)
//    }
//}
//
//fun Int.toDigitAndCarry(): Pair<Digit, Int> = Digit(this % 10) to this / 10
//
//data class HugeInt(val digits: List<Digit>) {
//    val nDigits = digits.size
//    operator fun get(i: Int) = digits.getOrNull(i) ?: Digit(0)
//    override fun toString() = if (digits.isEmpty()) "0" else digits.reversed().joinToString(separator = "")
//    operator fun plus(other: HugeInt) = HugeInt(buildList {
//        var carry = 0
//        var index = 0 // todo com
//        while (true) {
//            if (carry == 0 && index>= maxOf(nDigits, other.nDigits))
//                break@while
//
//            val sum = this[index].toInt() + other[index].toInt() + carry
//            val (sumDigit, newCarry) = sum.toDigitAndCarry()
//            add(sumDigit)
//            newCarry =
//        }
//    })
//}

class HugeInt(val digits:Array<Byte>) {

}