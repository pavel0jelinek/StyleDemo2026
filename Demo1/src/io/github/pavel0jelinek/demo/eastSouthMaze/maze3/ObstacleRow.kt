package io.github.pavel0jelinek.demo.eastSouthMaze.maze3

/**
 * Describes a row in a maze of free tiles and obstacles.
 *
 * This interface decouples the computation of the legal paths from a representation of obstacle list.
 */
interface ObstacleRow {
    /**
     * For out of bounds, returns `null`. Returning `true` might simplify the path-computing code,
     * but it would introduce an unhealthy coupling: If we create this interface, we may as well created
     * it well-defined and independent of the problem where we use it.
     */
    fun isObstacle(x: Int): Boolean?
}

/**
 * An implementation efficient when there are many obstacles - represents a row as a sequence of bits,
 * compressed as Array<ULong>.
 */
class CompressedObstacleRow(private val compressed: Array<ULong>, private val length: Int) : ObstacleRow {

    private constructor(obstacles: Iterable<Boolean>) : this(
        compress(obstacles), obstacles.count()
    )

    override fun isObstacle(x: Int): Boolean? =
        // Implemented by Mistral LLM.
        if (x in 0..<length) (compressed[x ushr 6] and (1UL shl (x and 63))) != 0UL
        else null

    companion object {
        private fun compress(obstacles: Iterable<Boolean>): Array<ULong> {
            val bits = obstacles.toList()
            val words = Array<ULong>((bits.size + 63) / 64) { 0u }
            bits.forEachIndexed { i, b ->
                if (b) words[i ushr 6] = words[i ushr 6] or (1UL shl (i and 63))
            }
            return words
        }
    }
}

/**
 * An implementation memory-efficient when the obstacles are sparse.
 */
//class SparseObstacleRow private constructor(
//    private val length: Int,
//    private val obstacles: IntArray,
//) : ObstacleRow {
//
//    private constructor(obstacles: Iterable<Boolean>) : this(
//        obstacles.count(),
//        obstacles.withIndex().filter { it.value }.map { it.index }.toIntArray().also { it.sort() }
//    )
//
//    override fun isObstacle(x: Int): Boolean? =
//        if (x < 0 || x >= length) true
//        else binarySearch(x) >= 0
//
//    private fun binarySearch(x: Int): Int =
//        obstacles.binarySearch(x)
//
//    companion object {
//        /** Efektivní tovární metoda pro řídké řádky. */
//        fun sparse(length: Int, obstacleIndices: IntArray): ObstacleRowImpl {
//            require(obstacleIndices.all { it in 0 until length })
//            return ObstacleRowImpl(length, obstacleIndices.apply { sort() }.distinct())
//        }
//    }
//}