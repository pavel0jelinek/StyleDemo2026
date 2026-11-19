package io.github.pavel0jelinek.styleDemo2026.oneWayMaze.maze

class CompressedBooleans private constructor(val size: Int, private val compressed: Array<ULong>) {

    constructor(bools: Iterable<Boolean>) : this(
        bools.count(),
        compress(bools)
    )

    operator fun get(index: Int): Boolean? =
        if (index in 0 until size) (compressed[index ushr 6] and (1UL shl (index and 63))) != 0UL
        else null

    fun toBools(): List<Boolean> =
        List(size) { i ->
            (compressed[i ushr 6] and (1UL shl (i and 63))) != 0UL
        }

    companion object {
        private fun compress(walls: Iterable<Boolean>): Array<ULong> {
            val bits = walls.toList()
            val words = Array<ULong>((bits.size + 63) / 64) { 0u }
            bits.forEachIndexed { i, b ->
                if (b) words[i ushr 6] = words[i ushr 6] or (1UL shl (i and 63))
            }
            return words
        }
    }
}