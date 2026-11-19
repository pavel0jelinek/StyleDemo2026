package io.github.pavel0jelinek.styleDemo2026.oneWayMaze

import java.math.BigInteger

/**
 * Function [io.github.pavel0jelinek.styleDemo2026.oneWayMaze.oneWayMaze1] was very slow. In a modern programming language,
 * it can be sped up easily by wrapping the computation inside a cache.
 *
 * This decreases the time complexity on an MxN maze from `O(2^min(M,N))` to `O(M*N)`.
 *
 * Also, let's use [BigInteger] because the range of [Long] can easily be exceeded.
 */
fun oneWayMaze2(
    walls: List<List<Boolean>>,
    x: Int = walls[0].lastIndex,
    y: Int = walls.lastIndex,
    cache: MutableMap<Pair<Int, Int>, BigInteger> = mutableMapOf()
): BigInteger = when {
    // A wall or out-of-bounds.
    walls.getOrNull(y)?.getOrNull(x) ?: true -> BigInteger.ZERO
    // Starting point
    x == 0 && y == 0 -> BigInteger.ONE
    else -> cache.getOrPut(x to y) {
        // We can arrive from the west or from the north
        oneWayMaze2(walls, x - 1, y, cache) +
                oneWayMaze2(walls, x, y - 1, cache)
    }
}

