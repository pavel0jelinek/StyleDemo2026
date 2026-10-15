package io.github.pavel0jelinek.StyleDemo2026.oneWayMaze.oneWayMaze1996

/**
 * The competition where I encountered this problem was held in 1995/96, we programmed in Pascal or C.
 * There were no such things like BigInteger or MutableMap, if we did not implement them ourselves.
 *
 *  I will use none of my helpers except [MyBigInt]. And I will use a simple interface [WallRow]
 *  (able to return whether a tile contains an wall) to
 *  - separate the data representation of the maze from the problem solution
 *  - and to allow for memory-efficient data representations.
 *
 */


/**
 *
 * Constructs the y-th row of path counts, assuming [previousRow] contains the (y-1)-th row.
 *
 * More formally speaking: Returns a list whose x-th element
 * contains the number of legal paths from the north-west corner to (x,y),
 * assuming that, for all x, previousRow[x] contains the number of legal paths to (x, y-1).
 * */
private fun Maze.computeRowFromPrevious(y: Int, previousRow:List<MyBigInt>): List<MyBigInt> = buildList {
    for (x in indicesX) {
        val nPathsToXY = if (isWall(x, y)!!) MyBigInt.ZERO
        else
        // Number of paths from the north
            previousRow[x] +
                    // Number of paths from the west is the last element of the currently constructed list.
                    (lastOrNull() ?: MyBigInt.ZERO)
        add(nPathsToXY)
    }
}

fun Maze.oneWayMaze1996(): MyBigInt {
    // Row of legal paths from (0,0) to (x, -1). No such paths exist.
    var currentRow =  List(sizeX) { MyBigInt.ZERO }
    // No need to store all computed rows in the memory, we need just the last computed one.
    for (y in 0..<sizeY) {
        currentRow = computeRowFromPrevious(y, currentRow)
    }
    return currentRow.last()
}
