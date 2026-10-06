package io.github.pavel0jelinek.demo.hanoi

import io.github.pavel0jelinek.demo.hanoi.Move
import kotlin.collections.plus

/**
 * Towers of Hanoi is a typical problem for recursion:
 * - Three rods, several disks of different size (diameter).
 * - A disk may never lie on a smaller disk.
 * - Each disk must be placed on one of the rods.
 * - An allowed "move" is a transfer of a single top disk of one rod on top of another rod.
 *
 * Problem: All disks are on rod A, get all of them on rod B.
 * For N disks, the shortest solution has exactly 2^N-1 moves, which can be proven by induction over N:
 * - A solution with 2^N-1 moves exists: transfer all disks except the largest to rod C (2^(N-1)-1 moves),
 *   move the largest from A to B (1 move), and then get the remaining ones onto B (2^(N-1)-1 moves).
 * - There is no shorter solution: the largest disk can move from A to B only when all N-1 smaller disks
 *   are on C, which takes at least 2^(N-1)-1 moves (induction hypothesis). Afterwards, they must get
 *   from C onto B, which again takes at least 2^(N-1)-1 moves. Together with the move of the largest
 *   disk, that is at least 2^N-1.
 **
 * ### A short recursive solution on ca. 5 lines of code:
 *
 * Prints how to move the [nDisks] smallest disks from rod [from] onto rod [onto], using rod [helper] as a helper.
 *
 * Correct in any phase of the solution (for N rods), assuming that nDisks<=N and all [nDisks] smallest disks are not on rod [from].
 */

fun printMoves(nDisks: Int, from: Char = 'A', onto: Char = 'B', helper: Char = 'C') {
    if (nDisks <= 0) return
    printMoves(nDisks - 1, from, helper, onto)
    print("$from->$onto;")
    printMoves(nDisks - 1, helper, onto, from)
}

/**
 * However, coupling the output with the algorithm is a bad design:
 * What if we want, e.g., to write the moves to a file, or to animate them?
 *
 * We should return a data structure, so here it is:
 */
enum class Rod { A, B, C }

/**
 * Represents moving the top disk from rod [from] onto rod [onto].
 */
data class Move(val from: Rod, val onto: Rod) {
    override fun toString() = "$from->$onto"
}

/**
 * Returns moves needed to move [nDisks] top disks from [from] onto [onto],
 * under the assumption that all [nDisks] smallest disks (i.e., all disks with numbers in 0..<[nDisks])
 * are now placed on [from].
 *
 * I think it is elegantly functional-style, without variables etc., but it comes as a cost:
 * it often copies a [List], which is slow. Even [java.util.LinkedList] will probably not help.
 * * We would need a persistent collection or code our own linked list.
 * * Or append a mutable collection, see [getMovesThroughAppending].
 */
fun getMovesFunctionally(nDisks: Int, from: Rod = Rod.A, onto: Rod = Rod.B, helper: Rod = Rod.C): List<Move> =
    if (nDisks <= 0) emptyList()
    else getMovesFunctionally(nDisks - 1, from, helper, onto) +
            Move(from, onto) +
            getMovesFunctionally(nDisks - 1, helper, onto, from)

/**
 * Returns a solution of Hanoi, constructed by appending to a mutable collection.
 *
 * Almost identical to [getMovesFunctionally], but faster.
 */
fun getMovesThroughAppending(nDisks: Int): List<Move> =
    mutableListOf<Move>().also { list ->
        appendMoves(nDisks, Rod.A, Rod.B, Rod.C, list)
    }

/**
 * Appends into [target] the sequence of moves needed to move [nDisks] from [from] onto [onto].
 */
private fun appendMoves(nDisks: Int, from: Rod, onto: Rod, helper: Rod, target: MutableList<Move>) {
    if (nDisks <= 0) return
    appendMoves(nDisks - 1, from, helper, onto, target)
    target.add(Move(from, onto))
    appendMoves(nDisks - 1, helper, onto, from, target)
}


/**
 * And now a solution without recursion. We need some terminology:
 *
 * - For N disks, let's number the disks from 0 (smallest) to N-1 (largest).
 * - Let's use "transfer" for moving a single disk, including all necessary moves of smaller disks.
 *   Note that these moves do _not_ depend on the position of larger disks.
 */
private class Transfer(
    val from: Rod,
    val onto: Rod,
    val over: Rod,
    var nSubTransfersCompleted: Int = 0
)

/**
 * Returns the list of moves needed to transfer [nDisks] disks from rod A onto rod B.
 */
fun getMovesWithoutRecursion(nDisks: Int): List<Move> {
    val moves = mutableListOf<Move>()
    // Contains all transfers currently in progress (started but unfinished).
    // `transfers[j]` represents the transfer of disk `nDisks-1-j`
    // Initialize it by placing exactly the transfer that the problem requires to perform.
    val transfers = mutableListOf(
        Transfer(Rod.A, Rod.B, Rod.C)
    )

    // while we still have unfinished work...
    while (transfers.isNotEmpty()) {
        // The transfer of the smallest disk out of all transfers in progress.
        val smallest = transfers.last()

        when (smallest.nSubTransfersCompleted) {
            0 ->
                // No disk to move - we are done here.
                if (transfers.size > nDisks) smallest.nSubTransfersCompleted = 2
                // Before continuing, move all smaller disks onto 'over'.
                else transfers.add(Transfer(smallest.from, smallest.over, smallest.onto))

            1 -> {
                // The first sub-transfer is complete. Now make the required move.
                moves.add(Move(smallest.from, smallest.onto))
                // And announce the necessity of the second sub-transfer.
                transfers.add(Transfer(smallest.over, smallest.onto, smallest.from))
            }

            2 -> {
                // All done at this level.
                transfers.removeAt(transfers.lastIndex)
                // Announce to the parent (if one exists) that one of its sub-transfers has just been completed.
                transfers.lastOrNull()?.also { it.nSubTransfersCompleted++ }
            }
        }
    }
    return moves
}

/**
 * Prints the outcome of all three versions.
 */
fun main() {
    println("Five-line version")
    printMoves(3)
    println("\nFunctional version:")
    println(getMovesFunctionally(3))
    println("\nAppending version:")
    println(getMovesThroughAppending(3))
    println("\nVersion without recursion:")
    println(getMovesWithoutRecursion(3))
}