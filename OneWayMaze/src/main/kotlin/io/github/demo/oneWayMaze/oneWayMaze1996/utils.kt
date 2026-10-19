package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996


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
