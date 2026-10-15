package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996


class CompressedBooleans(val size: Int, private val compressed: Array<ULong>) {

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

/**
 * Returns the lowest element in [this] satisfying the [predicate], or `null` if none exists.
 *
 * Uses a binary search, assuming that, with each satisfying integer, all higher integer also satisfy [predicate].
 */
fun ClosedRange<Int>.binaryFindLowestWhich(predicate: (Int) -> Boolean): Int? = if (isEmpty()) null
else {
    val mid = (start + endInclusive) / 2
    if (predicate(mid))
        (start..<mid).binaryFindLowestWhich(predicate) ?: mid
    else ((mid + 1)..endInclusive).binaryFindLowestWhich(predicate)
}
